package kr.osj.livving.data.network.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kr.osj.livving.core.platform.livvingLogD
import kr.osj.livving.data.network.NetworkConfig
import kr.osj.livving.data.network.dto.NotificationEventDto
import kr.osj.livving.data.network.dto.PushTokenDto
import kr.osj.livving.data.network.dto.TestNotificationResultDto
import kr.osj.livving.domain.livving.LivvingNotification
import kr.osj.livving.domain.livving.LivvingNotificationType
import kr.osj.livving.domain.livving.PushTokenRegistration
import kr.osj.livving.domain.livving.TestNotificationResult
import kr.osj.livving.domain.livving.repository.NotificationRepository
import kotlinx.serialization.json.Json

class SupabaseNotificationRepository(
    private val client: SupabaseClient,
    private val httpClient: HttpClient,
    private val config: NetworkConfig,
) : NotificationRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun registerPushToken(userId: String, registration: PushTokenRegistration) {
        client.from("push_tokens")
            .upsert(
                PushTokenDto(
                    userId = userId,
                    token = registration.token,
                    platform = registration.platform,
                    deviceId = registration.deviceId,
                    enabled = true,
                ),
            ) {
                onConflict = "token"
            }
    }

    override suspend fun disablePushToken(userId: String, token: String) {
        client.from("push_tokens")
            .update(mapOf("enabled" to false)) {
                filter {
                    filter("user_id", FilterOperator.EQ, userId)
                    filter("token", FilterOperator.EQ, token)
                }
            }
    }

    override suspend fun getNotifications(userId: String): List<LivvingNotification> {
        return client.from("notification_events")
            .select {
                filter {
                    filter("recipient_user_id", FilterOperator.EQ, userId)
                }
                order("created_at", Order.DESCENDING)
                limit(50)
            }
            .decodeList<NotificationEventDto>()
            .filter { it.status != "read" }
            .map { it.toDomain() }
    }

    override suspend fun markNotificationRead(userId: String, notificationId: String) {
        client.from("notification_events")
            .update(mapOf("status" to "read")) {
                filter {
                    filter("id", FilterOperator.EQ, notificationId)
                    filter("recipient_user_id", FilterOperator.EQ, userId)
                }
            }
    }

    override suspend fun sendTestNotification(userId: String): TestNotificationResult {
        check(userId.isNotBlank()) { "User id is required to send a test notification" }
        client.auth.awaitInitialization()
        runCatching {
            client.auth.refreshCurrentSession()
        }.onSuccess {
            livvingLogD("LivvingPushTest", "session refreshed before test notification userId=$userId")
        }.onFailure { throwable ->
            livvingLogD("LivvingPushTest", "session refresh skipped or failed ${throwable.message}")
        }
        val accessToken = client.auth.currentAccessTokenOrNull()
            ?: error("Supabase session is required to send a test notification")
        livvingLogD(
            tag = "LivvingPushTest",
            message = "request userId=$userId url=${config.supabaseClientUrl}/functions/v1/send-test-notification " +
                "accessTokenPrefix=${accessToken.take(12)} jwtParts=${accessToken.count { it == '.' } + 1}",
        )
        val response = httpClient.post {
            expectSuccess = false
            url("${config.supabaseClientUrl}/functions/v1/send-test-notification")
            headers {
                remove(HttpHeaders.Authorization)
                append(HttpHeaders.Authorization, "Bearer $accessToken")
                remove("apikey")
                append("apikey", config.supabaseAnonKey)
            }
        }
        val rawBody = response.bodyAsText()
        livvingLogD(
            tag = "LivvingPushTest",
            message = "response status=${response.status.value} body=$rawBody",
        )
        if (response.status.value !in 200..299) {
            error("Test notification failed: status=${response.status.value}, body=$rawBody")
        }
        val dto = json.decodeFromString<TestNotificationResultDto>(rawBody)
        return TestNotificationResult(
            tokenCount = dto.tokenCount,
            sentCount = dto.sentCount,
            failedCount = dto.failedCount,
            disabledTokenCount = dto.disabledTokenCount,
            firstError = dto.firstError,
        )
    }

    private fun NotificationEventDto.toDomain(): LivvingNotification {
        return LivvingNotification(
            id = id,
            type = when (eventType) {
                "missed_check_in" -> LivvingNotificationType.MissedCheckIn
                "guardian_request" -> LivvingNotificationType.GuardianRequest
                "relation_accepted" -> LivvingNotificationType.RelationAccepted
                "test_push" -> LivvingNotificationType.TestPush
                else -> LivvingNotificationType.Unknown
            },
            title = title,
            body = body,
            createdAt = createdAt,
            actorUserId = actorUserId,
            relatedUserId = relatedUserId,
            readAt = readAt ?: status.takeIf { it == "read" },
        )
    }
}
