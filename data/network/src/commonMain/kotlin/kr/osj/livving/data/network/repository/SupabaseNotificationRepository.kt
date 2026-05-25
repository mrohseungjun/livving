package kr.osj.livving.data.network.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kr.osj.livving.data.network.dto.NotificationEventDto
import kr.osj.livving.domain.livving.LivvingNotification
import kr.osj.livving.domain.livving.LivvingNotificationType
import kr.osj.livving.domain.livving.PushTokenRegistration
import kr.osj.livving.domain.livving.repository.NotificationRepository

class SupabaseNotificationRepository(
    private val client: SupabaseClient,
) : NotificationRepository {
    override suspend fun registerPushToken(userId: String, registration: PushTokenRegistration) {
        client.from("push_tokens")
            .upsert(
                mapOf(
                    "user_id" to userId,
                    "token" to registration.token,
                    "platform" to registration.platform,
                    "device_id" to registration.deviceId,
                    "enabled" to true,
                ),
            ) {
                onConflict = "token"
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

    private fun NotificationEventDto.toDomain(): LivvingNotification {
        return LivvingNotification(
            id = id,
            type = when (eventType) {
                "missed_check_in" -> LivvingNotificationType.MissedCheckIn
                "guardian_request" -> LivvingNotificationType.GuardianRequest
                "relation_accepted" -> LivvingNotificationType.RelationAccepted
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
