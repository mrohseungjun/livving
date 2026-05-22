package kr.osj.livving.feature.main

import androidx.navigation3.runtime.NavKey
import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianInvite
import kr.osj.livving.domain.livving.GuardianInviteRequest
import kr.osj.livving.domain.livving.GuardianStatus
import kr.osj.livving.domain.livving.LivvingUser
import kr.osj.livving.domain.livving.WatchingUser
import kr.osj.livving.feature.relations.RelationsTab
import kotlinx.serialization.Serializable

data class MainState(
    val sessionChecked: Boolean = false,
    val startRoute: MainRoute = MainRoute.Login,
    val currentUser: LivvingUser? = null,
    val deadline: String = "08:30",
    val selectedDeadline: String = "08:30",
    val delayMinutes: Int = 5,
    val checked: Boolean = false,
    val lastCheckedAt: String = "",
    val status: CheckInStatus = CheckInStatus.Before,
    val guardians: List<Guardian> = emptyList(),
    val watchingUsers: List<WatchingUser> = emptyList(),
    val activeInvites: List<GuardianInvite> = emptyList(),
    val inviteRequest: GuardianInviteRequest? = null,
    val pendingInviteCode: String? = null,
    val terms: TermsState = TermsState(),
    val relationTab: RelationsTab = RelationsTab.MyGuardians,
    val pushEnabled: Boolean = true,
    val relationPushEnabled: Boolean = true,
    val missedPushEnabled: Boolean = true,
)

data class TermsState(
    val service: Boolean = true,
    val privacy: Boolean = true,
    val age: Boolean = true,
    val marketing: Boolean = false,
) {
    val required: Boolean get() = service && privacy && age
    val all: Boolean get() = service && privacy && age && marketing
}

@Serializable
sealed interface MainRoute : NavKey {
    @Serializable
    data object Splash : MainRoute
    @Serializable
    data object Login : MainRoute
    @Serializable
    data object Terms : MainRoute
    @Serializable
    data object SetupDeadline : MainRoute
    @Serializable
    data object SetupDelay : MainRoute
    @Serializable
    data object Home : MainRoute
    @Serializable
    data object History : MainRoute
    @Serializable
    data object Relations : MainRoute
    @Serializable
    data object Invite : MainRoute
    @Serializable
    data object InviteStatus : MainRoute
    @Serializable
    data object GuardianDetail : MainRoute
    @Serializable
    data object Notifications : MainRoute
    @Serializable
    data object Alert : MainRoute
    @Serializable
    data object Request : MainRoute
    @Serializable
    data object Settings : MainRoute
    @Serializable
    data object Schedule : MainRoute
    @Serializable
    data object Profile : MainRoute
    @Serializable
    data object Privacy : MainRoute
    @Serializable
    data object DeadlineChange : MainRoute
    @Serializable
    data object DelaySetting : MainRoute
}

enum class TermsItem {
    Service,
    Privacy,
    Age,
    Marketing,
}
