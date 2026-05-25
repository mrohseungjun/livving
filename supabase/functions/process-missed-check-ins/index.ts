import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

type PendingMessage = {
  event_id: string;
  recipient_user_id: string;
  related_user_id: string;
  token: string;
  platform: "android" | "ios";
  title: string;
  body: string;
};

type ServiceAccount = {
  client_email: string;
  private_key: string;
  project_id?: string;
};

const jsonHeaders = { "content-type": "application/json" };

Deno.serve(async (request) => {
  const cronSecret = Deno.env.get("LIVVING_CRON_SECRET");
  if (cronSecret && request.headers.get("x-livving-cron-secret") !== cronSecret) {
    return new Response(JSON.stringify({ error: "unauthorized" }), {
      status: 401,
      headers: jsonHeaders,
    });
  }

  const supabaseUrl = Deno.env.get("SUPABASE_URL");
  const serviceKey = getSupabaseSecretKey();
  const serviceAccountJson = Deno.env.get("FCM_SERVICE_ACCOUNT_JSON");

  if (!supabaseUrl || !serviceKey || !serviceAccountJson) {
    return new Response(JSON.stringify({ error: "missing server configuration" }), {
      status: 500,
      headers: jsonHeaders,
    });
  }

  const serviceAccount = JSON.parse(serviceAccountJson) as ServiceAccount;
  const projectId = Deno.env.get("FCM_PROJECT_ID") || serviceAccount.project_id;
  if (!projectId) {
    return new Response(JSON.stringify({ error: "missing FCM project id" }), {
      status: 500,
      headers: jsonHeaders,
    });
  }

  const supabase = createClient(supabaseUrl, serviceKey);
  const { data, error } = await supabase.rpc("process_missed_check_ins");
  if (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 500,
      headers: jsonHeaders,
    });
  }

  const accessToken = await createGoogleAccessToken(serviceAccount);
  const messages = (data ?? []) as PendingMessage[];
  const sentEventIds = new Set<string>();
  const failedEvents = new Map<string, string>();

  for (const message of messages) {
    const result = await sendFcmMessage(projectId, accessToken, message);
    if (result.ok) {
      sentEventIds.add(message.event_id);
    } else if (!sentEventIds.has(message.event_id)) {
      failedEvents.set(message.event_id, result.error);
    }
  }

  await Promise.all([
    ...Array.from(sentEventIds).map((eventId) =>
      supabase
        .from("notification_events")
        .update({ status: "sent", sent_at: new Date().toISOString(), error_message: null })
        .eq("id", eventId)
    ),
    ...Array.from(failedEvents.entries()).map(([eventId, errorMessage]) =>
      supabase
        .from("notification_events")
        .update({ status: "failed", error_message: errorMessage })
        .eq("id", eventId)
    ),
  ]);

  return new Response(
    JSON.stringify({ queued: messages.length, sent: sentEventIds.size, failed: failedEvents.size }),
    { headers: jsonHeaders },
  );
});

function getSupabaseSecretKey(): string | undefined {
  const keys = Deno.env.get("SUPABASE_SECRET_KEYS");
  if (keys) {
    const parsed = JSON.parse(keys) as Record<string, string>;
    return parsed.service_role ?? Object.values(parsed)[0];
  }
  return Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
}

async function sendFcmMessage(
  projectId: string,
  accessToken: string,
  message: PendingMessage,
): Promise<{ ok: true } | { ok: false; error: string }> {
  const response = await fetch(`https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`, {
    method: "POST",
    headers: {
      "authorization": `Bearer ${accessToken}`,
      "content-type": "application/json",
    },
    body: JSON.stringify({
      message: {
        token: message.token,
        notification: {
          title: message.title,
          body: message.body,
        },
        data: {
          eventId: message.event_id,
          type: "missed_check_in",
          relatedUserId: message.related_user_id,
        },
        android: {
          priority: "high",
          notification: {
            channel_id: "livving_alerts",
          },
        },
        apns: {
          headers: {
            "apns-priority": "10",
            "apns-push-type": "alert",
          },
          payload: {
            aps: {
              alert: {
                title: message.title,
                body: message.body,
              },
              sound: "default",
            },
          },
        },
      },
    }),
  });

  if (response.ok) return { ok: true };
  return { ok: false, error: await response.text() };
}

async function createGoogleAccessToken(serviceAccount: ServiceAccount): Promise<string> {
  const now = Math.floor(Date.now() / 1000);
  const assertion = await signJwt(
    {
      alg: "RS256",
      typ: "JWT",
    },
    {
      iss: serviceAccount.client_email,
      scope: "https://www.googleapis.com/auth/firebase.messaging",
      aud: "https://oauth2.googleapis.com/token",
      iat: now,
      exp: now + 3600,
    },
    serviceAccount.private_key,
  );

  const response = await fetch("https://oauth2.googleapis.com/token", {
    method: "POST",
    headers: { "content-type": "application/x-www-form-urlencoded" },
    body: new URLSearchParams({
      grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
      assertion,
    }),
  });

  if (!response.ok) {
    throw new Error(await response.text());
  }

  const json = await response.json() as { access_token: string };
  return json.access_token;
}

async function signJwt(header: Record<string, unknown>, payload: Record<string, unknown>, privateKey: string) {
  const encodedHeader = base64UrlEncode(JSON.stringify(header));
  const encodedPayload = base64UrlEncode(JSON.stringify(payload));
  const signingInput = `${encodedHeader}.${encodedPayload}`;
  const key = await crypto.subtle.importKey(
    "pkcs8",
    pemToArrayBuffer(privateKey),
    { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
    false,
    ["sign"],
  );
  const signature = await crypto.subtle.sign(
    "RSASSA-PKCS1-v1_5",
    key,
    new TextEncoder().encode(signingInput),
  );
  return `${signingInput}.${base64UrlEncode(signature)}`;
}

function pemToArrayBuffer(pem: string): ArrayBuffer {
  const base64 = pem
    .replace("-----BEGIN PRIVATE KEY-----", "")
    .replace("-----END PRIVATE KEY-----", "")
    .replaceAll("\\n", "")
    .replaceAll("\n", "")
    .trim();
  const binary = atob(base64);
  const bytes = new Uint8Array(binary.length);
  for (let index = 0; index < binary.length; index += 1) {
    bytes[index] = binary.charCodeAt(index);
  }
  return bytes.buffer;
}

function base64UrlEncode(value: string | ArrayBuffer): string {
  const bytes = typeof value === "string" ? new TextEncoder().encode(value) : new Uint8Array(value);
  let binary = "";
  for (const byte of bytes) {
    binary += String.fromCharCode(byte);
  }
  return btoa(binary).replaceAll("+", "-").replaceAll("/", "_").replaceAll("=", "");
}
