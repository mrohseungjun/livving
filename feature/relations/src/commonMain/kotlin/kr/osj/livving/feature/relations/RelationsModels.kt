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
)

enum class WatchingState {
    Safe,
    Waiting,
    Missed,
}

data class WatchingUserUiModel(
    val id: Long,
    val name: String,
    val state: WatchingState,
    val text: String,
    val sub: String,
)
