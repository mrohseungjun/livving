package kr.osj.livving.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kr.osj.livving.core.ui.LivvingBottomBar
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.addLivvingMinutes
import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.GuardianStatus
import kr.osj.livving.feature.auth.AuthTermsState
import kr.osj.livving.feature.auth.LoginScreen
import kr.osj.livving.feature.auth.TermsScreen
import kr.osj.livving.feature.home.HomeScreen
import kr.osj.livving.feature.notifications.AlertScreen
import kr.osj.livving.feature.notifications.NotificationsScreen
import kr.osj.livving.feature.notifications.RequestScreen
import kr.osj.livving.feature.relations.GuardianDetailScreen
import kr.osj.livving.feature.relations.InviteScreen
import kr.osj.livving.feature.relations.InviteStatusScreen
import kr.osj.livving.feature.relations.RelationGuardianStatus
import kr.osj.livving.feature.relations.RelationGuardianUiModel
import kr.osj.livving.feature.relations.RelationsScreen
import kr.osj.livving.feature.relations.RelationsTab
import kr.osj.livving.feature.relations.WatchingState
import kr.osj.livving.feature.relations.WatchingUserUiModel
import kr.osj.livving.feature.settings.HistoryScreen
import kr.osj.livving.feature.settings.PrivacyScreen
import kr.osj.livving.feature.settings.ProfileScreen
import kr.osj.livving.feature.settings.ScheduleScreen
import kr.osj.livving.feature.settings.SettingsScreen
import kr.osj.livving.feature.setup.DeadlineScreen
import kr.osj.livving.feature.setup.DelayScreen
import kr.osj.livving.feature.splash.SplashScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel

private val mainNavigationStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(MainRoute.Splash.serializer())
            subclass(MainRoute.Login.serializer())
            subclass(MainRoute.Terms.serializer())
            subclass(MainRoute.SetupDeadline.serializer())
            subclass(MainRoute.SetupDelay.serializer())
            subclass(MainRoute.Home.serializer())
            subclass(MainRoute.History.serializer())
            subclass(MainRoute.Relations.serializer())
            subclass(MainRoute.Invite.serializer())
            subclass(MainRoute.InviteStatus.serializer())
            subclass(MainRoute.GuardianDetail.serializer())
            subclass(MainRoute.Notifications.serializer())
            subclass(MainRoute.Alert.serializer())
            subclass(MainRoute.Request.serializer())
            subclass(MainRoute.Settings.serializer())
            subclass(MainRoute.Schedule.serializer())
            subclass(MainRoute.Profile.serializer())
            subclass(MainRoute.Privacy.serializer())
            subclass(MainRoute.DeadlineChange.serializer())
            subclass(MainRoute.DelaySetting.serializer())
        }
    }
}

@Composable
fun MainRoute(
    initialInviteCode: String? = null,
    onShareText: (String) -> Unit = {},
    viewModel: MainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val backStack = rememberNavBackStack(mainNavigationStateConfiguration, MainRoute.Splash)

    LaunchedEffect(initialInviteCode) {
        if (initialInviteCode != null) {
            viewModel.openInvite(initialInviteCode)
        }
    }

    LaunchedEffect(state.sessionChecked, state.startRoute) {
        if (state.sessionChecked && backStack.lastOrNull() == MainRoute.Splash) {
            backStack.replaceWith(state.startRoute)
        }
    }

    LaunchedEffect(state.inviteRequest) {
        if (state.inviteRequest != null && backStack.lastOrNull() != MainRoute.Request) {
            backStack.navigateTo(MainRoute.Request)
        }
    }

    MainScreen(
        state = state,
        backStack = backStack,
        onIntent = viewModel::onIntent,
        onNavigate = { route -> backStack.navigateTo(route) },
        onReplace = { route -> backStack.replaceWith(route) },
        onBack = { backStack.popOrReplace(MainRoute.Home) },
        onLoginSuccess = { onReady -> viewModel.refreshSessionAfterLogin(onReady) },
        onSaveInitialSettings = { onSaved -> viewModel.saveInitialSettings(onSaved) },
        onShareText = onShareText,
    )
}

@Composable
fun MainScreen(
    state: MainState,
    backStack: List<NavKey>,
    onIntent: (MainIntent) -> Unit,
    onNavigate: (MainRoute) -> Unit,
    onReplace: (MainRoute) -> Unit,
    onBack: () -> Unit,
    onLoginSuccess: ((MainRoute) -> Unit) -> Unit,
    onSaveInitialSettings: (() -> Unit) -> Unit,
    onShareText: (String) -> Unit,
) {
    MainNavDisplay(
        state = state,
        backStack = backStack,
        onIntent = onIntent,
        onNavigate = onNavigate,
        onReplace = onReplace,
        onBack = onBack,
        onLoginSuccess = onLoginSuccess,
        onSaveInitialSettings = onSaveInitialSettings,
        onShareText = onShareText,
    )
}

@Composable
private fun MainNavDisplay(
    state: MainState,
    backStack: List<NavKey>,
    onIntent: (MainIntent) -> Unit,
    onNavigate: (MainRoute) -> Unit,
    onReplace: (MainRoute) -> Unit,
    onBack: () -> Unit,
    onLoginSuccess: ((MainRoute) -> Unit) -> Unit,
    onSaveInitialSettings: (() -> Unit) -> Unit,
    onShareText: (String) -> Unit,
) {
    NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryProvider = entryProvider {
            entry<MainRoute.Splash> {
                MainEntry(MainRoute.Splash, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Login> {
                MainEntry(MainRoute.Login, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Terms> {
                MainEntry(MainRoute.Terms, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.SetupDeadline> {
                MainEntry(MainRoute.SetupDeadline, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.SetupDelay> {
                MainEntry(MainRoute.SetupDelay, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Home> {
                MainEntry(MainRoute.Home, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.History> {
                MainEntry(MainRoute.History, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Relations> {
                MainEntry(MainRoute.Relations, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Invite> {
                MainEntry(MainRoute.Invite, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.InviteStatus> {
                MainEntry(MainRoute.InviteStatus, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.GuardianDetail> {
                MainEntry(MainRoute.GuardianDetail, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Notifications> {
                MainEntry(MainRoute.Notifications, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Alert> {
                MainEntry(MainRoute.Alert, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Request> {
                MainEntry(MainRoute.Request, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Settings> {
                MainEntry(MainRoute.Settings, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Schedule> {
                MainEntry(MainRoute.Schedule, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Profile> {
                MainEntry(MainRoute.Profile, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.Privacy> {
                MainEntry(MainRoute.Privacy, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.DeadlineChange> {
                MainEntry(MainRoute.DeadlineChange, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
            entry<MainRoute.DelaySetting> {
                MainEntry(MainRoute.DelaySetting, state, onIntent, onNavigate, onReplace, onBack, onLoginSuccess, onSaveInitialSettings, onShareText)
            }
        },
    )
}

@Composable
private fun MainEntry(
    route: MainRoute,
    state: MainState,
    onIntent: (MainIntent) -> Unit,
    onNavigate: (MainRoute) -> Unit,
    onReplace: (MainRoute) -> Unit,
    onBack: () -> Unit,
    onLoginSuccess: ((MainRoute) -> Unit) -> Unit,
    onSaveInitialSettings: (() -> Unit) -> Unit,
    onShareText: (String) -> Unit,
) {
    when (route) {
        MainRoute.Splash -> SplashScreen(
            onFinished = {
                if (state.sessionChecked) {
                    onReplace(state.startRoute)
                }
            },
        )
        else -> MainEntryContainer(
            route = route,
            onReplace = onReplace,
        ) {
            MainEntryContent(
                route = route,
                state = state,
                onIntent = onIntent,
                onNavigate = onNavigate,
                onReplace = onReplace,
                onBack = onBack,
                onLoginSuccess = onLoginSuccess,
                onSaveInitialSettings = onSaveInitialSettings,
                onShareText = onShareText,
            )
        }
    }
}

@Composable
private fun MainEntryContainer(
    route: MainRoute,
    onReplace: (MainRoute) -> Unit,
    content: @Composable () -> Unit,
) {
    LivvingScrollableScreen(
        bottomBar = if (route.hasBottomBar()) {
            {
                LivvingBottomBar(
                    items = listOf(
                        "home" to "홈",
                        "relations" to "관계",
                        "notifications" to "알림",
                        "settings" to "설정",
                    ),
                    active = route.activeTab(),
                    onClick = { tab ->
                        onReplace(
                            when (tab) {
                                "relations" -> MainRoute.Relations
                                "notifications" -> MainRoute.Notifications
                                "settings" -> MainRoute.Settings
                                else -> MainRoute.Home
                            },
                        )
                    },
                )
            }
        } else {
            null
        },
        content = content,
    )
}

@Composable
private fun MainEntryContent(
    route: MainRoute,
    state: MainState,
    onIntent: (MainIntent) -> Unit,
    onNavigate: (MainRoute) -> Unit,
    onReplace: (MainRoute) -> Unit,
    onBack: () -> Unit,
    onLoginSuccess: ((MainRoute) -> Unit) -> Unit,
    onSaveInitialSettings: (() -> Unit) -> Unit,
    onShareText: (String) -> Unit,
) {
    when (route) {
        MainRoute.Splash -> Unit
        MainRoute.Login -> LoginScreen(
            onLoginSuccess = {
                onLoginSuccess { route ->
                    onReplace(route)
                }
            },
        )
        MainRoute.Terms -> TermsScreen(
            state = AuthTermsState(
                service = state.terms.service,
                privacy = state.terms.privacy,
                age = state.terms.age,
                marketing = state.terms.marketing,
            ),
            onBackClick = onBack,
            onToggleAllClick = { onIntent(MainIntent.ToggleAllTerms) },
            onToggleServiceClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Service)) },
            onTogglePrivacyClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Privacy)) },
            onToggleAgeClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Age)) },
            onToggleMarketingClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Marketing)) },
            onContinueClick = { onNavigate(MainRoute.SetupDeadline) },
        )
        MainRoute.SetupDeadline -> DeadlineScreen(
            selectedDeadline = state.selectedDeadline,
            fromSettings = false,
            onBackClick = onBack,
            onDeadlineClick = { onIntent(MainIntent.SelectDeadline(it)) },
            onSaveClick = {
                onIntent(MainIntent.SaveDeadline(false))
                onNavigate(MainRoute.SetupDelay)
            },
        )
        MainRoute.SetupDelay -> DelayScreen(
            deadline = state.deadline,
            delayMinutes = state.delayMinutes,
            fromSettings = false,
            onBackClick = onBack,
            onDelayClick = { onIntent(MainIntent.SelectDelay(it)) },
            onSaveClick = {
                onIntent(MainIntent.SaveDelay(false))
                onSaveInitialSettings {
                    onReplace(MainRoute.Home)
                }
            },
        )
        MainRoute.Home -> HomeScreen(
            deadline = state.deadline,
            delayMinutes = state.delayMinutes,
            checked = state.checked,
            lastCheckedAt = state.lastCheckedAt,
            late = state.status == CheckInStatus.Late,
            acceptedGuardianNames = state.guardians.filter { it.status == GuardianStatus.Accepted }.map { it.name },
            onNotificationClick = { onNavigate(MainRoute.Notifications) },
            onCheckInClick = { onIntent(MainIntent.CompleteCheckIn) },
            onRelationsClick = { onReplace(MainRoute.Relations) },
            onHistoryClick = { onNavigate(MainRoute.History) },
            onToggleLateClick = { onIntent(MainIntent.ToggleLateState) },
        )
        MainRoute.History -> HistoryScreen(onBackClick = onBack)
        MainRoute.Relations -> RelationsScreen(
            selectedTab = state.relationTab,
            guardians = state.guardians.map { guardian ->
                RelationGuardianUiModel(
                    id = guardian.id,
                    name = guardian.name,
                    relation = guardian.relation,
                    status = if (guardian.status == GuardianStatus.Pending) {
                        RelationGuardianStatus.Pending
                    } else {
                        RelationGuardianStatus.Accepted
                    },
                )
            },
            watchingUsers = state.watchingUsers.map { user ->
                val status = when (user.status) {
                    CheckInStatus.Done -> WatchingState.Safe
                    CheckInStatus.Late -> WatchingState.Missed
                    CheckInStatus.Before -> WatchingState.Waiting
                }
                WatchingUserUiModel(
                    id = user.id.hashCode().toLong(),
                    name = user.name,
                    state = status,
                    text = when (status) {
                        WatchingState.Safe -> "오늘 안부 확인 완료"
                        WatchingState.Waiting -> "아직 안부 확인 전"
                        WatchingState.Missed -> "안부 미확인"
                    },
                    sub = when (status) {
                        WatchingState.Safe -> user.lastCheckedAt.ifBlank { "오늘 확인" }
                        WatchingState.Waiting -> "마감 대기 중"
                        WatchingState.Missed -> "보호자 알림 대상"
                    },
                )
            },
            deadline = state.deadline,
            alertAt = addLivvingMinutes(state.deadline, state.delayMinutes),
            onMyGuardiansClick = { onIntent(MainIntent.SelectRelationTab(RelationsTab.MyGuardians)) },
            onWatchingClick = { onIntent(MainIntent.SelectRelationTab(RelationsTab.Watching)) },
            onGuardianClick = { guardian ->
                onNavigate(
                    if (guardian.status == RelationGuardianStatus.Pending) {
                        MainRoute.InviteStatus
                    } else {
                        MainRoute.GuardianDetail
                    },
                )
            },
            onInviteClick = { onNavigate(MainRoute.Invite) },
            onWatchingUserClick = { user ->
                if (user.state == WatchingState.Missed) {
                    onNavigate(MainRoute.Alert)
                } else {
                    onReplace(MainRoute.Home)
                }
            },
        )
        MainRoute.Invite -> InviteScreen(
            inviteLink = state.activeInvites.firstOrNull()?.inviteLink,
            pendingCount = state.guardians.count { it.status == GuardianStatus.Pending },
            acceptedCount = state.guardians.count { it.status == GuardianStatus.Accepted },
            onBackClick = onBack,
            onCreateInviteClick = { onIntent(MainIntent.CreateInvite) },
            onShareInviteClick = {
                val inviteLink = state.activeInvites.firstOrNull()?.inviteLink
                if (inviteLink == null) {
                    onIntent(MainIntent.CreateInvite)
                } else {
                    onShareText("livving 보호자 초대 링크\n$inviteLink")
                }
            },
        )
        MainRoute.InviteStatus -> InviteStatusScreen(onBackClick = onBack)
        MainRoute.GuardianDetail -> GuardianDetailScreen(
            deadline = state.deadline,
            delayMinutes = state.delayMinutes,
            onBackClick = onBack,
        )
        MainRoute.Notifications -> NotificationsScreen(
            deadline = state.deadline,
            alertAt = addLivvingMinutes(state.deadline, state.delayMinutes),
            onAlertClick = { onNavigate(MainRoute.Alert) },
            onRequestClick = { onNavigate(MainRoute.Request) },
        )
        MainRoute.Alert -> AlertScreen(
            deadline = state.deadline,
            alertAt = addLivvingMinutes(state.deadline, state.delayMinutes),
            onBackClick = onBack,
            onConfirmClick = { onReplace(MainRoute.Notifications) },
        )
        MainRoute.Request -> RequestScreen(
            ownerName = state.inviteRequest?.ownerName ?: "알 수 없는 사용자",
            onBackClick = onBack,
            onAcceptClick = {
                onIntent(MainIntent.AcceptInvite)
                onReplace(MainRoute.Relations)
            },
            onRejectClick = {
                onIntent(MainIntent.RejectInvite)
                onReplace(MainRoute.Home)
            },
        )
        MainRoute.Settings -> SettingsScreen(
            deadline = state.deadline,
            delayMinutes = state.delayMinutes,
            pushEnabled = state.pushEnabled,
            relationPushEnabled = state.relationPushEnabled,
            missedPushEnabled = state.missedPushEnabled,
            onDeadlineClick = {
                onIntent(MainIntent.SelectDeadline(state.deadline))
                onNavigate(MainRoute.DeadlineChange)
            },
            onDelayClick = { onNavigate(MainRoute.DelaySetting) },
            onScheduleClick = { onNavigate(MainRoute.Schedule) },
            onPushToggleClick = { onIntent(MainIntent.TogglePush) },
            onRelationPushToggleClick = { onIntent(MainIntent.ToggleRelationPush) },
            onMissedPushToggleClick = { onIntent(MainIntent.ToggleMissedPush) },
            onProfileClick = { onNavigate(MainRoute.Profile) },
            onPrivacyClick = { onNavigate(MainRoute.Privacy) },
            onLogoutClick = { onReplace(MainRoute.Login) },
        )
        MainRoute.Schedule -> ScheduleScreen(
            deadline = state.deadline,
            delayMinutes = state.delayMinutes,
            onBackClick = onBack,
        )
        MainRoute.Profile -> ProfileScreen(onBackClick = onBack)
        MainRoute.Privacy -> PrivacyScreen(onBackClick = onBack)
        MainRoute.DeadlineChange -> DeadlineScreen(
            selectedDeadline = state.selectedDeadline,
            fromSettings = true,
            onBackClick = onBack,
            onDeadlineClick = { onIntent(MainIntent.SelectDeadline(it)) },
            onSaveClick = {
                onIntent(MainIntent.SaveDeadline(true))
                onReplace(MainRoute.Settings)
            },
        )
        MainRoute.DelaySetting -> DelayScreen(
            deadline = state.deadline,
            delayMinutes = state.delayMinutes,
            fromSettings = true,
            onBackClick = onBack,
            onDelayClick = { onIntent(MainIntent.SelectDelay(it)) },
            onSaveClick = {
                onIntent(MainIntent.SaveDelay(true))
                onReplace(MainRoute.Settings)
            },
        )
    }
}

private fun MutableList<NavKey>.navigateTo(route: MainRoute) {
    if (lastOrNull() != route) {
        add(route)
    }
}

private fun MutableList<NavKey>.replaceWith(route: MainRoute) {
    clear()
    add(route)
}

private fun MutableList<NavKey>.popOrReplace(fallback: MainRoute) {
    if (size > 1) {
        removeAt(lastIndex)
    } else {
        replaceWith(fallback)
    }
}

private fun MainRoute.hasBottomBar(): Boolean = this !in setOf(
    MainRoute.Splash,
    MainRoute.Login,
    MainRoute.Terms,
    MainRoute.SetupDeadline,
    MainRoute.SetupDelay,
)

private fun MainRoute.activeTab(): String = when (this) {
    MainRoute.Relations,
    MainRoute.Invite,
    MainRoute.InviteStatus,
    MainRoute.GuardianDetail,
    MainRoute.Request -> "relations"
    MainRoute.Notifications,
    MainRoute.Alert -> "notifications"
    MainRoute.Settings,
    MainRoute.Schedule,
    MainRoute.Profile,
    MainRoute.Privacy,
    MainRoute.DeadlineChange,
    MainRoute.DelaySetting -> "settings"
    else -> "home"
}
