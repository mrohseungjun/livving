import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

type PushToken = {
  id: number;
  token: string;
  platform: "android" | "ios";
};

type ServiceAccount = {
  client_email: string;
  private_key: string;
  project_id?: string;
};

type AuthUser = {
  id?: string;
};

type Profile = {
  nickname: string;
};

type UserSettings = {
  push_enabled: boolean;
  relation_push_enabled: boolean;
};

type CheckInRequestReservation = {
  event_id: string | null;
  guardian_relation_id: number | null;
  already_checked_in: boolean;
  throttled: boolean;
  retry_after_seconds: number | null;
};

const jsonHeaders = { "content-type": "application/json" };

Deno.serve(async (request) => {
  if (request.method !== "POST") {
    return jsonResponse({ error: "method not allowed" }, 405);
  }

  const supabaseUrl = Deno.env.get("SUPABASE_URL");
  const serviceKey = getSupabaseSecretKey();
  const authKey = Deno.env.get("SUPABASE_ANON_KEY") ?? Deno.env.get("SUPABASE_PUBLISHABLE_KEY");
  const serviceAccountJson = Deno.env.get("FCM_SERVICE_ACCOUNT_JSON");
  if (!supabaseUrl || !serviceKey || !authKey || !serviceAccountJson) {
    return jsonResponse({ error: "missing server configuration" }, 500);
  }

  const authorization = request.headers.get("authorization") ?? "";
  const userAccessToken = extractUserBearerToken(authorization);
  if (!userAccessToken) {
    return jsonResponse({ error: "missing authorization" }, 401);
  }

  const userResult = await getUserByAccessToken(supabaseUrl, authKey, userAccessToken);
  if (userResult instanceof Response) {
    return userResult;
  }
  const guardianUserId = userResult.id;
  if (!guardianUserId) {
    return jsonResponse({ error: "invalid authorization", detail: "user id is missing" }, 401);
  }

  const body = await request.json().catch(() => ({})) as Record<string, unknown>;
  const targetUserId = String(body.target_user_id ?? "").trim();
  const message = String(body.message ?? "").trim().slice(0, 120);
  if (!targetUserId || !message) {
    return jsonResponse({ error: "target_user_id and message are required" }, 400);
  }

  const supabase = createClient(supabaseUrl, serviceKey);
  const { data: guardianProfile, error: profileError } = await supabase
    .from("profiles")
    .select("nickname")
    .eq("id", guardianUserId)
    .maybeSingle();
  if (profileError) {
    return jsonResponse({ error: profileError.message }, 500);
  }
  const profile = guardianProfile as Profile | null;
  const title = `${profile?.nickname ?? "보호자"}님의 안부 확인 요청`;

  const { data: reservation, error: reservationError } = await supabase
    .rpc("reserve_check_in_request_notification", {
      p_guardian_user_id: guardianUserId,
      p_target_user_id: targetUserId,
      p_title: title,
      p_body: message,
    })
    .maybeSingle();
  if (reservationError) {
    return jsonResponse({ error: reservationError.message }, 500);
  }
  const checkInRequestReservation = reservation as CheckInRequestReservation | null;
  if (!checkInRequestReservation) {
    return jsonResponse({ error: "accepted guardian relation is required" }, 403);
  }
  if (checkInRequestReservation.already_checked_in) {
    return jsonResponse({
      sent_count: 0,
      failed_count: 0,
      throttled: false,
      already_checked_in: true,
    });
  }
  if (checkInRequestReservation.throttled) {
    return jsonResponse({
      sent_count: 0,
      failed_count: 0,
      throttled: true,
      retry_after_seconds: checkInRequestReservation.retry_after_seconds ?? 60,
    });
  }
  if (!checkInRequestReservation.event_id) {
    return jsonResponse({ error: "check-in request reservation failed" }, 500);
  }

  const [{ data: targetSettings, error: settingsError }, { data: tokens, error: tokenError }] = await Promise.all([
    supabase
      .from("user_settings")
      .select("push_enabled, relation_push_enabled")
      .eq("user_id", targetUserId)
      .maybeSingle(),
    supabase
      .from("push_tokens")
      .select("id, token, platform")
      .eq("user_id", targetUserId)
      .eq("enabled", true),
  ]);
  if (settingsError) {
    return jsonResponse({ error: settingsError.message }, 500);
  }
  if (tokenError) {
    return jsonResponse({ error: tokenError.message }, 500);
  }

  const settings = targetSettings as UserSettings | null;
  const pushAllowed = settings?.push_enabled !== false && settings?.relation_push_enabled !== false;
  const pushTokens = pushAllowed ? (tokens ?? []) as PushToken[] : [];

  const serviceAccount = JSON.parse(serviceAccountJson) as ServiceAccount;
  const projectId = Deno.env.get("FCM_PROJECT_ID") || serviceAccount.project_id;
  const iosBundleId = Deno.env.get("IOS_BUNDLE_ID") || "kr.osj.livving";
  if (!projectId) {
    return jsonResponse({ error: "missing FCM project id" }, 500);
  }

  const accessToken = await createGoogleAccessToken(serviceAccount);
  const results = await Promise.all(
    pushTokens.map((pushToken) => sendFcmMessage(projectId, accessToken, iosBundleId, pushToken, title, message, guardianUserId)),
  );
  const sentCount = results.filter((result) => result.ok).length;
  const failedCount = results.length - sentCount;
  const firstError = results.find((result) => !result.ok)?.error ?? null;
  const invalidTokenIds = results
    .filter((result) => !result.ok && isInvalidFcmTokenError(result.error ?? ""))
    .map((result) => result.tokenId);

  const now = new Date();
  const { error: eventUpdateError } = await supabase
    .from("notification_events")
    .update({
      status: sentCount > 0 ? "sent" : "failed",
      sent_at: sentCount > 0 ? now.toISOString() : null,
      error_message: firstError,
    })
    .eq("id", checkInRequestReservation.event_id);
  if (eventUpdateError) {
    return jsonResponse({ error: eventUpdateError.message }, 500);
  }

  if (invalidTokenIds.length > 0) {
    const { error: tokenUpdateError } = await supabase
      .from("push_tokens")
      .update({ enabled: false, updated_at: now.toISOString() })
      .in("id", invalidTokenIds);
    if (tokenUpdateError) {
      return jsonResponse({ error: tokenUpdateError.message }, 500);
    }
  }

  return jsonResponse({
    sent_count: sentCount,
    failed_count: failedCount,
    throttled: false,
    already_checked_in: false,
    first_error: firstError,
  });
});

function jsonResponse(body: Record<string, unknown>, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: jsonHeaders,
  });
}

function getSupabaseSecretKey(): string | undefined {
  const keys = Deno.env.get("SUPABASE_SECRET_KEYS");
  if (keys) {
    const parsed = JSON.parse(keys) as Record<string, string>;
    return parsed.service_role ?? Object.values(parsed)[0];
  }
  return Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
}

function extractUserBearerToken(authorization: string): string {
  const matches = authorization.matchAll(/Bearer\s+([^,\s]+)/gi);
  let token = "";
  for (const match of matches) {
    token = match[1];
  }
  return token;
}

async function getUserByAccessToken(
  supabaseUrl: string,
  authKey: string,
  accessToken: string,
): Promise<AuthUser | Response> {
  const response = await fetch(`${supabaseUrl}/auth/v1/user`, {
    headers: {
      "apikey": authKey,
      "authorization": `Bearer ${accessToken}`,
    },
  });
  if (!response.ok) {
    const body = await response.text();
    console.error("send-check-in-request auth failed", response.status, body);
    return jsonResponse({ error: "invalid authorization", detail: body }, 401);
  }
  return await response.json() as AuthUser;
}

async function sendFcmMessage(
  projectId: string,
  accessToken: string,
  iosBundleId: string,
  pushToken: PushToken,
  title: string,
  body: string,
  guardianUserId: string,
): Promise<{ ok: boolean; tokenId: number; error?: string }> {
  const response = await fetch(`https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`, {
    method: "POST",
    headers: {
      "authorization": `Bearer ${accessToken}`,
      "content-type": "application/json",
    },
    body: JSON.stringify({
      message: {
        token: pushToken.token,
        notification: {
          title,
          body,
        },
        data: {
          type: "check_in_request",
          guardianUserId,
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
            "apns-topic": iosBundleId,
          },
          payload: {
            aps: {
              alert: {
                title,
                body,
              },
              sound: "default",
            },
          },
        },
      },
    }),
  });

  if (response.ok) return { ok: true, tokenId: pushToken.id };
  return { ok: false, tokenId: pushToken.id, error: await response.text() };
}

function isInvalidFcmTokenError(error: string): boolean {
  return error.includes("UNREGISTERED") ||
    error.includes("INVALID_ARGUMENT") ||
    error.includes("registration-token-not-registered");
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

  const body = await response.json() as { access_token: string };
  return body.access_token;
}

async function signJwt(header: Record<string, unknown>, payload: Record<string, unknown>, privateKeyPem: string): Promise<string> {
  const encodedHeader = base64UrlEncode(JSON.stringify(header));
  const encodedPayload = base64UrlEncode(JSON.stringify(payload));
  const unsignedToken = `${encodedHeader}.${encodedPayload}`;
  const key = await crypto.subtle.importKey(
    "pkcs8",
    pemToArrayBuffer(privateKeyPem),
    {
      name: "RSASSA-PKCS1-v1_5",
      hash: "SHA-256",
    },
    false,
    ["sign"],
  );
  const signature = await crypto.subtle.sign(
    "RSASSA-PKCS1-v1_5",
    key,
    new TextEncoder().encode(unsignedToken),
  );
  return `${unsignedToken}.${base64UrlEncode(signature)}`;
}

function pemToArrayBuffer(pem: string): ArrayBuffer {
  const normalized = pem
    .replace("-----BEGIN PRIVATE KEY-----", "")
    .replace("-----END PRIVATE KEY-----", "")
    .replace(/\s/g, "");
  const binary = atob(normalized);
  const bytes = new Uint8Array(binary.length);
  for (let i = 0; i < binary.length; i += 1) {
    bytes[i] = binary.charCodeAt(i);
  }
  return bytes.buffer;
}

function base64UrlEncode(value: string | ArrayBuffer): string {
  const bytes = typeof value === "string" ? new TextEncoder().encode(value) : new Uint8Array(value);
  let binary = "";
  bytes.forEach((byte) => {
    binary += String.fromCharCode(byte);
  });
  return btoa(binary)
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/g, "");
}
