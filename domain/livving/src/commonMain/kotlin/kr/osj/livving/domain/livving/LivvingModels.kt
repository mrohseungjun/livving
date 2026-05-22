package kr.osj.livving.domain.livving

data class Guardian(
    val id: Long,
    val name: String,
    val relation: String,
    val status: GuardianStatus,
    val inviteCode: String? = null,
)

data class GuardianInvite(
    val id: String,
    val inviteCode: String,
) {
    val inviteLink: String get() = "https://livving.app/join/$inviteCode"
}

data class GuardianInviteRequest(
    val inviteCode: String,
    val ownerUserId: String,
    val ownerName: String,
)

data class CheckInCompletion(
    val lastCheckedAt: String,
    val status: CheckInStatus,
)

enum class CheckInStatus {
    Before,
    Late,
    Done,
}

enum class GuardianStatus {
    Accepted,
    Pending,
}
