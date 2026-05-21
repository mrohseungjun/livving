package kr.osj.livving.domain.livving

data class Guardian(
    val id: Long,
    val name: String,
    val relation: String,
    val status: GuardianStatus,
    val inviteCode: String? = null,
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
