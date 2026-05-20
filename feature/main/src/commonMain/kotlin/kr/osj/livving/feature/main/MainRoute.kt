package kr.osj.livving.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import kr.osj.livving.feature.setup.DelayScreen
import kr.osj.livving.feature.setup.DeadlineScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainRoute(
    viewModel: MainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    MainScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun MainScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit,
) {
    LivvingScrollableScreen(
        bottomBar = if (state.route.hasBottomBar()) {
            {
                LivvingBottomBar(
                    items = listOf(
                        "home" to "홈",
                        "relations" to "관계",
                        "notifications" to "알림",
                        "settings" to "설정",
                    ),
                    active = state.route.activeTab(),
                    onClick = { tab ->
                        onIntent(
                            MainIntent.Navigate(
                                when (tab) {
                                    "relations" -> MainRoute.Relations
                                    "notifications" -> MainRoute.Notifications
                                    "settings" -> MainRoute.Settings
                                    else -> MainRoute.Home
                                },
                            ),
                        )
                    },
                )
            }
        } else {
            null
        },
    ) {
        when (state.route) {
            MainRoute.Login -> LoginScreen(
                onStartClick = { onIntent(MainIntent.Navigate(MainRoute.Terms)) },
            )
            MainRoute.Terms -> TermsScreen(
                state = AuthTermsState(
                    service = state.terms.service,
                    privacy = state.terms.privacy,
                    age = state.terms.age,
                    marketing = state.terms.marketing,
                ),
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Login)) },
                onToggleAllClick = { onIntent(MainIntent.ToggleAllTerms) },
                onToggleServiceClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Service)) },
                onTogglePrivacyClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Privacy)) },
                onToggleAgeClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Age)) },
                onToggleMarketingClick = { onIntent(MainIntent.ToggleTerms(TermsItem.Marketing)) },
                onContinueClick = { onIntent(MainIntent.Navigate(MainRoute.SetupDeadline)) },
            )
            MainRoute.SetupDeadline -> DeadlineScreen(
                selectedDeadline = state.selectedDeadline,
                fromSettings = false,
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Terms)) },
                onDeadlineClick = { onIntent(MainIntent.SelectDeadline(it)) },
                onSaveClick = { onIntent(MainIntent.SaveDeadline(false)) },
            )
            MainRoute.SetupDelay -> DelayScreen(
                deadline = state.deadline,
                delayMinutes = state.delayMinutes,
                fromSettings = false,
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.SetupDeadline)) },
                onDelayClick = { onIntent(MainIntent.SelectDelay(it)) },
                onSaveClick = { onIntent(MainIntent.SaveDelay(false)) },
            )
            MainRoute.Home -> HomeScreen(
                deadline = state.deadline,
                delayMinutes = state.delayMinutes,
                checked = state.checked,
                lastCheckedAt = state.lastCheckedAt,
                late = state.status == CheckInStatus.Late,
                acceptedGuardianNames = state.guardians.filter { it.status == GuardianStatus.Accepted }.map { it.name },
                onNotificationClick = { onIntent(MainIntent.Navigate(MainRoute.Notifications)) },
                onCheckInClick = { onIntent(MainIntent.CompleteCheckIn) },
                onRelationsClick = { onIntent(MainIntent.Navigate(MainRoute.Relations)) },
                onHistoryClick = { onIntent(MainIntent.Navigate(MainRoute.History)) },
                onToggleLateClick = { onIntent(MainIntent.ToggleLateState) },
            )
            MainRoute.History -> HistoryScreen(
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Home)) },
            )
            MainRoute.Relations -> RelationsScreen(
                selectedTab = state.relationTab,
                guardians = state.guardians.map { guardian ->
                    RelationGuardianUiModel(
                        id = guardian.id,
                        name = guardian.name,
                        relation = guardian.relation,
                        status = if (guardian.status == GuardianStatus.Pending) RelationGuardianStatus.Pending else RelationGuardianStatus.Accepted,
                    )
                },
                watchingUsers = seedWatchingUsers,
                deadline = state.deadline,
                alertAt = addLivvingMinutes(state.deadline, state.delayMinutes),
                onMyGuardiansClick = { onIntent(MainIntent.SelectRelationTab(RelationsTab.MyGuardians)) },
                onWatchingClick = { onIntent(MainIntent.SelectRelationTab(RelationsTab.Watching)) },
                onGuardianClick = { guardian ->
                    onIntent(MainIntent.Navigate(if (guardian.status == RelationGuardianStatus.Pending) MainRoute.InviteStatus else MainRoute.GuardianDetail))
                },
                onInviteClick = { onIntent(MainIntent.Navigate(MainRoute.Invite)) },
                onWatchingUserClick = { user ->
                    onIntent(MainIntent.Navigate(if (user.state == WatchingState.Missed) MainRoute.Alert else MainRoute.Home))
                },
            )
            MainRoute.Invite -> InviteScreen(
                pendingCount = state.guardians.count { it.status == GuardianStatus.Pending },
                acceptedCount = state.guardians.count { it.status == GuardianStatus.Accepted },
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Relations)) },
                onCreateInviteClick = { onIntent(MainIntent.CreateInvite) },
            )
            MainRoute.InviteStatus -> InviteStatusScreen(
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Relations)) },
            )
            MainRoute.GuardianDetail -> GuardianDetailScreen(
                deadline = state.deadline,
                delayMinutes = state.delayMinutes,
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Relations)) },
            )
            MainRoute.Notifications -> NotificationsScreen(
                deadline = state.deadline,
                alertAt = addLivvingMinutes(state.deadline, state.delayMinutes),
                onAlertClick = { onIntent(MainIntent.Navigate(MainRoute.Alert)) },
                onRequestClick = { onIntent(MainIntent.Navigate(MainRoute.Request)) },
            )
            MainRoute.Alert -> AlertScreen(
                deadline = state.deadline,
                alertAt = addLivvingMinutes(state.deadline, state.delayMinutes),
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Notifications)) },
                onConfirmClick = { onIntent(MainIntent.Navigate(MainRoute.Notifications)) },
            )
            MainRoute.Request -> RequestScreen(
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Notifications)) },
                onAcceptClick = { onIntent(MainIntent.Navigate(MainRoute.Relations)) },
                onRejectClick = { onIntent(MainIntent.Navigate(MainRoute.Notifications)) },
            )
            MainRoute.Settings -> SettingsScreen(
                deadline = state.deadline,
                delayMinutes = state.delayMinutes,
                pushEnabled = state.pushEnabled,
                relationPushEnabled = state.relationPushEnabled,
                missedPushEnabled = state.missedPushEnabled,
                onDeadlineClick = { onIntent(MainIntent.Navigate(MainRoute.DeadlineChange)) },
                onDelayClick = { onIntent(MainIntent.Navigate(MainRoute.DelaySetting)) },
                onScheduleClick = { onIntent(MainIntent.Navigate(MainRoute.Schedule)) },
                onPushToggleClick = { onIntent(MainIntent.TogglePush) },
                onRelationPushToggleClick = { onIntent(MainIntent.ToggleRelationPush) },
                onMissedPushToggleClick = { onIntent(MainIntent.ToggleMissedPush) },
                onProfileClick = { onIntent(MainIntent.Navigate(MainRoute.Profile)) },
                onPrivacyClick = { onIntent(MainIntent.Navigate(MainRoute.Privacy)) },
                onLogoutClick = { onIntent(MainIntent.Navigate(MainRoute.Login)) },
            )
            MainRoute.Schedule -> ScheduleScreen(
                deadline = state.deadline,
                delayMinutes = state.delayMinutes,
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Settings)) },
            )
            MainRoute.Profile -> ProfileScreen(
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Settings)) },
            )
            MainRoute.Privacy -> PrivacyScreen(
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Settings)) },
            )
            MainRoute.DeadlineChange -> DeadlineScreen(
                selectedDeadline = state.selectedDeadline,
                fromSettings = true,
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Settings)) },
                onDeadlineClick = { onIntent(MainIntent.SelectDeadline(it)) },
                onSaveClick = { onIntent(MainIntent.SaveDeadline(true)) },
            )
            MainRoute.DelaySetting -> DelayScreen(
                deadline = state.deadline,
                delayMinutes = state.delayMinutes,
                fromSettings = true,
                onBackClick = { onIntent(MainIntent.Navigate(MainRoute.Settings)) },
                onDelayClick = { onIntent(MainIntent.SelectDelay(it)) },
                onSaveClick = { onIntent(MainIntent.SaveDelay(true)) },
            )
        }
    }
}

private fun MainRoute.hasBottomBar(): Boolean = this !in setOf(
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

private val seedWatchingUsers = listOf(
    WatchingUserUiModel(1, "오승준", WatchingState.Safe, "오늘 안부 확인 완료", "08:25 확인"),
    WatchingUserUiModel(2, "김영희", WatchingState.Waiting, "아직 안부 확인 전", "마감 대기 중"),
    WatchingUserUiModel(3, "박민수", WatchingState.Missed, "안부 미확인", "보호자 알림 발송"),
)
