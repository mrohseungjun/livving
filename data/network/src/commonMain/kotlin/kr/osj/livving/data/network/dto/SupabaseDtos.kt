package kr.osj.livving.data.network.dto

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
data class GuardianRelationDto(
    val id: Long,
    @SerialName("guardian_name") val guardianName: String,
    val relation: String,
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
