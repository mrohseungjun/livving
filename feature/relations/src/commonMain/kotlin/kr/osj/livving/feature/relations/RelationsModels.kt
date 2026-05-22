package kr.osj.livving.feature.relations

enum class RelationsTab {
    MyGuardians,
    Watching,
}

enum class RelationGuardianStatus {
    Accepted,
    Pending,
}

data class RelationGuardianUiModel(
    val id: Long,
    val name: String,
    val relation: String,
    val status: RelationGuardianStatus,
    val phoneNumber: String? = null,
)

enum class WatchingState {
    Safe,
    Waiting,
    Missed,
}

data class WatchingUserUiModel(
    val id: Long,
    val userId: String,
    val name: String,
    val state: WatchingState,
    val text: String,
    val sub: String,
    val phoneNumber: String? = null,
)
