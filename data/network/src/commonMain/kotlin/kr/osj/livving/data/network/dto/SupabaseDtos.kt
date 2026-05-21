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
)
