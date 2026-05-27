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
  const user = userResult;
  if (!user.id) {
    return jsonResponse({ error: "invalid authorization", detail: "user id is missing" }, 401);
  }

  const supabase = createClient(supabaseUrl, serviceKey);
  const { data: tokens, error: tokenError } = await supabase
    .from("push_tokens")
    .select("id, token, platform")
    .eq("user_id", user.id)
    .eq("enabled", true);
  if (tokenError) {
    return jsonResponse({ error: tokenError.message }, 500);
  }

  const pushTokens = (tokens ?? []) as PushToken[];
  if (pushTokens.length === 0) {
    return jsonResponse({ token_count: 0, sent_count: 0, failed_count: 0 });
  }

  const serviceAccount = JSON.parse(serviceAccountJson) as ServiceAccount;
  const projectId = Deno.env.get("FCM_PROJECT_ID") || serviceAccount.project_id;
  const iosBundleId = Deno.env.get("IOS_BUNDLE_ID") || "kr.osj.livving";
  if (!projectId) {
    return jsonResponse({ error: "missing FCM project id" }, 500);
  }

  const accessToken = await createGoogleAccessToken(serviceAccount);
  const results = await Promise.all(
    pushTokens.map((pushToken) => sendFcmMessage(projectId, accessToken, iosBundleId, pushToken)),
  );
  const sentCount = results.filter((result) => result.ok).length;
  const failedCount = results.length - sentCount;
  const invalidTokenIds = results
    .filter((result) => !result.ok && isInvalidFcmTokenError(result.error ?? ""))
    .map((result) => result.tokenId);

  if (invalidTokenIds.length > 0) {
    await supabase
      .from("push_tokens")
      .update({ enabled: false, updated_at: new Date().toISOString() })
      .in("id", invalidTokenIds);
  }

  if (pushTokens.length > 0) {
    await supabase
      .from("notification_events")
      .insert({
        recipient_user_id: user.id,
        actor_user_id: user.id,
        event_type: "test_push",
        title: "livving 테스트 알림",
        body: sentCount > 0
          ? `테스트 알림을 ${sentCount}개 기기에 보냈어요.`
          : `테스트 알림 전송에 실패했어요. ${results.find((result) => !result.ok)?.error?.slice(0, 120) ?? ""}`.trim(),
        related_user_id: user.id,
        status: sentCount > 0 ? "sent" : "failed",
        sent_at: sentCount > 0 ? new Date().toISOString() : null,
      });
  }

  return jsonResponse({
    token_count: pushTokens.length,
    sent_count: sentCount,
    failed_count: failedCount,
    disabled_token_count: invalidTokenIds.length,
    results: results.map((result) => ({
      platform: result.platform,
      ok: result.ok,
      token_id: result.tokenId,
      error: result.ok ? null : result.error,
    })),
    first_error: results.find((result) => !result.ok)?.error ?? null,
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
  const tokens = Array.from(matches, (match) => match[1]?.trim()).filter(Boolean) as string[];
  for (let index = tokens.length - 1; index >= 0; index -= 1) {
    if (tokens[index].split(".").length === 3) {
      return tokens[index];
    }
  }
  return tokens.at(-1) ?? "";
}

async function getUserByAccessToken(
  supabaseUrl: string,
  authKey: string,
  accessToken: string,
): Promise<AuthUser | Response> {
  const response = await fetch(`${supabaseUrl}/auth/v1/user`, {
    headers: {
      "authorization": `Bearer ${accessToken}`,
      "apikey": authKey,
    },
  });
  if (!response.ok) {
    const body = await response.text();
    console.error("send-test-notification auth failed", response.status, body);
    return jsonResponse({ error: "invalid authorization", detail: body }, 401);
  }
  return await response.json() as AuthUser;
}

async function sendFcmMessage(
  projectId: string,
  accessToken: string,
  iosBundleId: string,
  pushToken: PushToken,
): Promise<{ ok: boolean; tokenId: number; platform: PushToken["platform"]; error?: string }> {
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
          title: "livving 테스트 알림",
          body: `${pushToken.platform.toUpperCase()} 기기로 테스트 알림을 보냈어요.`,
        },
        data: {
          type: "test_push",
          platform: pushToken.platform,
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
                title: "livving 테스트 알림",
                body: "iOS 기기로 테스트 알림을 보냈어요.",
              },
              sound: "default",
            },
          },
        },
      },
    }),
  });

  if (response.ok) return { ok: true, tokenId: pushToken.id, platform: pushToken.platform };
  return { ok: false, tokenId: pushToken.id, platform: pushToken.platform, error: await response.text() };
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
