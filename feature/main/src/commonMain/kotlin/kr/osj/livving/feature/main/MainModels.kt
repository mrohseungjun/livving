package kr.osj.livving.feature.main

import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianStatus
import kr.osj.livving.feature.relations.RelationsTab

data class MainState(
    val route: MainRoute = MainRoute.Login,
    val deadline: String = "08:30",
    val selectedDeadline: String = "08:30",
    val delayMinutes: Int = 5,
    val checked: Boolean = false,
    val lastCheckedAt: String = "",
    val status: CheckInStatus = CheckInStatus.Before,
    val guardians: List<Guardian> = seedGuardians,
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

enum class MainRoute {
    Login,
    Terms,
    SetupDeadline,
    SetupDelay,
    Home,
    History,
    Relations,
    Invite,
    InviteStatus,
    GuardianDetail,
    Notifications,
    Alert,
    Request,
    Settings,
    Schedule,
    Profile,
    Privacy,
    DeadlineChange,
    DelaySetting,
}

enum class TermsItem {
    Service,
    Privacy,
    Age,
    Marketing,
}

val seedGuardians = listOf(
    Guardian(1, "김지연", "딸", GuardianStatus.Accepted),
    Guardian(2, "이민호", "친구", GuardianStatus.Accepted),
    Guardian(3, "박선영", "이웃", GuardianStatus.Pending),
)
