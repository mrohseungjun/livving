package kr.osj.livving.domain.livving

data class Guardian(
    val id: Long,
    val name: String,
    val relation: String,
    val status: GuardianStatus,
    val inviteCode: String? = null,
    val phoneNumber: String? = null,
)

data class GuardianInvite(
    val id: String,
    val inviteCode: String,
) {
    val inviteLink: String get() = "livving://join/$inviteCode"
}

data class GuardianInviteRequest(
    val inviteCode: String,
    val ownerUserId: String,
    val ownerName: String,
)

data class WatchingUser(
    val id: String,
    val name: String,
    val status: CheckInStatus = CheckInStatus.Before,
    val lastCheckedAt: String = "",
    val phoneNumber: String? = null,
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
