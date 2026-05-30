package kr.osj.livving.data.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    @SerialName("kakao_id") val kakaoId: String,
    val nickname: String,
    @SerialName("profile_image_url") val profileImageUrl: String? = null,
)

@Serializable
data class UserContactSettingDto(
    @SerialName("user_id") val userId: String,
    @SerialName("phone_number") val phoneNumber: String? = null,
    @SerialName("phone_call_enabled") val phoneCallEnabled: Boolean = false,
)

@Serializable
data class PushTokenDto(
    @SerialName("user_id") val userId: String,
    val token: String,
    val platform: String,
    @SerialName("device_id") val deviceId: String? = null,
    val enabled: Boolean = true,
)

@Serializable
data class TestNotificationResultDto(
    @SerialName("token_count") val tokenCount: Int = 0,
    @SerialName("sent_count") val sentCount: Int = 0,
    @SerialName("failed_count") val failedCount: Int = 0,
    @SerialName("disabled_token_count") val disabledTokenCount: Int = 0,
    @SerialName("first_error") val firstError: String? = null,
)

@Serializable
data class SendCheckInRequestDto(
    @SerialName("target_user_id") val targetUserId: String,
    val message: String,
)

@Serializable
data class CheckInRequestResultDto(
    @SerialName("sent_count") val sentCount: Int = 0,
    @SerialName("failed_count") val failedCount: Int = 0,
    val throttled: Boolean = false,
    @SerialName("retry_after_seconds") val retryAfterSeconds: Int? = null,
    @SerialName("already_checked_in") val alreadyCheckedIn: Boolean = false,
    @SerialName("first_error") val firstError: String? = null,
)

@Serializable
data class NotificationEventDto(
    val id: String,
    @SerialName("recipient_user_id") val recipientUserId: String,
    @SerialName("actor_user_id") val actorUserId: String? = null,
    @SerialName("event_type") val eventType: String,
    val title: String,
    val body: String,
    @SerialName("related_user_id") val relatedUserId: String? = null,
    val status: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("read_at") val readAt: String? = null,
)

@Serializable
data class GuardianRelationDto(
    val id: Long,
    @SerialName("user_id") val userId: String,
    @SerialName("guardian_user_id") val guardianUserId: String? = null,
    @SerialName("guardian_name") val guardianName: String,
    val relation: String,
    val status: String,
    @SerialName("invite_code") val inviteCode: String,
    @SerialName("invite_link_id") val inviteLinkId: String? = null,
)

@Serializable
data class GuardianRelationCreateDto(
    @SerialName("user_id") val userId: String,
    @SerialName("guardian_user_id") val guardianUserId: String,
    @SerialName("guardian_name") val guardianName: String,
    val relation: String,
    val status: String,
    @SerialName("invite_code") val inviteCode: String,
    @SerialName("invite_link_id") val inviteLinkId: String,
)

@Serializable
data class InviteLinkDto(
    val id: String,
    @SerialName("owner_user_id") val ownerUserId: String,
    @SerialName("invite_code") val inviteCode: String,
    val status: String,
)

@Serializable
data class InviteRequestDto(
    val id: String,
    @SerialName("owner_user_id") val ownerUserId: String,
    @SerialName("invite_code") val inviteCode: String,
    val status: String,
)

@Serializable
data class CheckInDto(
    val id: Long,
    @SerialName("checked_at") val checkedAt: String,
    @SerialName("check_in_date") val checkInDate: String? = null,
)

@Serializable
data class UserSettingsDto(
    @SerialName("user_id") val userId: String,
    @SerialName("deadline_time") val deadlineTime: String,
    @SerialName("delay_minutes") val delayMinutes: Int,
    @SerialName("push_enabled") val pushEnabled: Boolean,
    @SerialName("relation_push_enabled") val relationPushEnabled: Boolean,
    @SerialName("missed_push_enabled") val missedPushEnabled: Boolean,
)
