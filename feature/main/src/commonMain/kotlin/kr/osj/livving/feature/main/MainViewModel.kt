package kr.osj.livving.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.InitialUserSettings
import kr.osj.livving.domain.livving.LivvingNotificationType
import kr.osj.livving.domain.livving.PushTokenRegistration
import kr.osj.livving.domain.livving.WatchingUser
import kr.osj.livving.domain.livving.usecase.AcceptGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.CompleteCheckInUseCase
import kr.osj.livving.domain.livving.usecase.CreateGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.DisablePushTokenUseCase
import kr.osj.livving.domain.livving.usecase.DisconnectGuardianUseCase
import kr.osj.livving.domain.livving.usecase.GetActiveInviteLinksUseCase
import kr.osj.livving.domain.livving.usecase.GetCurrentAuthSessionUseCase
import kr.osj.livving.domain.livving.usecase.GetGuardianInviteRequestByOwnerUseCase
import kr.osj.livving.domain.livving.usecase.GetGuardianInviteRequestUseCase
import kr.osj.livving.domain.livving.usecase.GetMyGuardiansUseCase
import kr.osj.livving.domain.livving.usecase.GetNotificationsUseCase
import kr.osj.livving.domain.livving.usecase.GetTodayCheckInUseCase
import kr.osj.livving.domain.livving.usecase.GetWatchingUsersUseCase
import kr.osj.livving.domain.livving.usecase.LogoutUseCase
import kr.osj.livving.domain.livving.usecase.MarkNotificationReadUseCase
import kr.osj.livving.domain.livving.usecase.RegisterPushTokenUseCase
import kr.osj.livving.domain.livving.usecase.SaveInitialUserSettingsUseCase
import kr.osj.livving.domain.livving.usecase.SavePhoneContactUseCase
import kr.osj.livving.domain.livving.usecase.SendTestNotificationUseCase
import kr.osj.livving.domain.livving.usecase.ToggleLateCheckInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val completeCheckInUseCase: CompleteCheckInUseCase,
    private val toggleLateCheckInUseCase: ToggleLateCheckInUseCase,
    private val createGuardianInviteUseCase: CreateGuardianInviteUseCase,
    private val getMyGuardiansUseCase: GetMyGuardiansUseCase,
    private val getActiveInviteLinksUseCase: GetActiveInviteLinksUseCase,
    private val getGuardianInviteRequestUseCase: GetGuardianInviteRequestUseCase,
    private val getGuardianInviteRequestByOwnerUseCase: GetGuardianInviteRequestByOwnerUseCase,
    private val acceptGuardianInviteUseCase: AcceptGuardianInviteUseCase,
    private val disconnectGuardianUseCase: DisconnectGuardianUseCase,
    private val getWatchingUsersUseCase: GetWatchingUsersUseCase,
    private val getCurrentAuthSessionUseCase: GetCurrentAuthSessionUseCase,
    private val getTodayCheckInUseCase: GetTodayCheckInUseCase,
    private val saveInitialUserSettingsUseCase: SaveInitialUserSettingsUseCase,
    private val savePhoneContactUseCase: SavePhoneContactUseCase,
    private val registerPushTokenUseCase: RegisterPushTokenUseCase,
    private val disablePushTokenUseCase: DisablePushTokenUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase,
    private val sendTestNotificationUseCase: SendTestNotificationUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        loadSession()
    }

    fun onIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.CompleteCheckIn -> completeCheckIn()
            MainIntent.ToggleLateState -> toggleLateState()
            is MainIntent.SelectDeadline -> update { it.copy(selectedDeadline = intent.time) }
            is MainIntent.SaveDeadline -> saveDeadline(intent.fromSettings)
            is MainIntent.SelectDelay -> update { it.copy(delayMinutes = intent.minutes) }
            is MainIntent.SaveDelay -> saveCurrentSettings()
            is MainIntent.ToggleTerms -> toggleTerms(intent.item)
            MainIntent.ToggleAllTerms -> toggleAllTerms()
            is MainIntent.SelectRelationTab -> update { it.copy(relationTab = intent.tab) }
            is MainIntent.SelectGuardian -> update { it.copy(selectedGuardianId = intent.guardianId) }
            is MainIntent.SelectWatchingUser -> update { it.copy(selectedWatchingUserId = intent.userId) }
            MainIntent.RefreshRelations -> refreshRelations()
            MainIntent.DisconnectSelectedGuardian -> disconnectSelectedGuardian()
            is MainIntent.ChangePhoneNumber -> update { it.copy(phoneNumberInput = intent.value) }
            MainIntent.TogglePhoneCallEnabled -> update { it.copy(phoneCallEnabled = !it.phoneCallEnabled) }
            MainIntent.SavePhoneContact -> savePhoneContact()
            is MainIntent.RegisterPushToken -> registerPushToken(intent.token)
            MainIntent.LoadNotifications -> loadNotifications()
            is MainIntent.SelectNotification -> selectNotification(intent.notificationId)
            MainIntent.CreateInvite -> createInvite()
            is MainIntent.ChangeManualInviteCode -> update {
                it.copy(
                    manualInviteCode = intent.value,
                    manualInviteError = null,
                )
            }
            MainIntent.SubmitManualInviteCode -> openManualInviteCode()
            MainIntent.AcceptInvite -> acceptInvite()
            MainIntent.RejectInvite -> update { it.copy(inviteRequest = null, pendingInviteCode = null) }
            MainIntent.TogglePush -> toggleAndSaveSettings { it.copy(pushEnabled = !it.pushEnabled) }
            MainIntent.ToggleRelationPush -> toggleAndSaveSettings { it.copy(relationPushEnabled = !it.relationPushEnabled) }
            MainIntent.ToggleMissedPush -> toggleAndSaveSettings { it.copy(missedPushEnabled = !it.missedPushEnabled) }
            MainIntent.SendTestNotification -> sendTestNotification()
            MainIntent.Logout -> logout()
        }
    }

    private fun completeCheckIn() {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            runCatching { completeCheckInUseCase(userId) }
                .onSuccess { completion ->
                    update {
                        it.copy(
                            checked = true,
                            lastCheckedAt = completion.lastCheckedAt,
                            status = completion.status,
                        )
                    }
                }
        }
    }

    private fun toggleLateState() {
        update {
            it.copy(
                checked = false,
                status = toggleLateCheckInUseCase(it.status),
            )
        }
    }

    private fun saveDeadline(fromSettings: Boolean) {
        update {
            it.copy(
                deadline = it.selectedDeadline,
            )
        }
        if (fromSettings) {
            saveCurrentSettings()
        }
    }

    private fun toggleTerms(item: TermsItem) {
        update {
            val terms = it.terms
            it.copy(
                terms = when (item) {
                    TermsItem.Service -> terms.copy(service = !terms.service)
                    TermsItem.Privacy -> terms.copy(privacy = !terms.privacy)
                    TermsItem.Age -> terms.copy(age = !terms.age)
                    TermsItem.Marketing -> terms.copy(marketing = !terms.marketing)
                },
            )
        }
    }

    private fun toggleAllTerms() {
        update {
            val next = !it.terms.all
            it.copy(terms = TermsState(next, next, next, next))
        }
    }

    private fun createInvite() {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            runCatching { createGuardianInviteUseCase(userId) }
                .onSuccess { invite ->
                    update {
                        it.copy(activeInvites = listOf(invite))
                    }
                }
        }
    }

    private fun refreshRelations() {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            val guardians = runCatching { getMyGuardiansUseCase(userId) }.getOrNull().orEmpty()
            val watchingUsers = loadWatchingUsers(userId)
            val activeInvites = runCatching { getActiveInviteLinksUseCase(userId) }.getOrNull().orEmpty()
            update {
                it.copy(
                    guardians = guardians,
                    watchingUsers = watchingUsers,
                    activeInvites = activeInvites,
                )
            }
        }
    }

    private fun disconnectSelectedGuardian() {
        viewModelScope.launch {
            val current = _state.value
            val userId = current.currentUser?.id ?: return@launch
            val guardianRelationId = current.selectedGuardianId ?: return@launch
            runCatching { disconnectGuardianUseCase(userId, guardianRelationId) }
                .onSuccess {
                    val guardians = runCatching { getMyGuardiansUseCase(userId) }.getOrNull().orEmpty()
                    update {
                        it.copy(
                            guardians = guardians,
                            selectedGuardianId = null,
                        )
                    }
                }
        }
    }

    private fun loadSession() {
        viewModelScope.launch {
            val session = getCurrentAuthSessionUseCase()
            val todayCheckIn = session?.user?.id
                ?.let { userId -> runCatching { getTodayCheckInUseCase(userId) }.getOrNull() }
            val guardians = session?.user?.id
                ?.let { userId -> runCatching { getMyGuardiansUseCase(userId) }.getOrNull() }
                .orEmpty()
            val watchingUsers = session?.user?.id
                ?.let { userId -> loadWatchingUsers(userId) }
                .orEmpty()
            val activeInvites = session?.user?.id
                ?.let { userId -> runCatching { getActiveInviteLinksUseCase(userId) }.getOrNull() }
                .orEmpty()
            val notifications = session?.user?.id
                ?.let { userId -> runCatching { getNotificationsUseCase(userId) }.getOrNull() }
                .orEmpty()
            val inviteRequest = _state.value.pendingInviteCode
                ?.let { inviteCode -> runCatching { getGuardianInviteRequestUseCase(inviteCode) }.getOrNull() }
            update {
                it.copy(
                    sessionChecked = true,
                    currentUser = session?.user,
                    phoneNumberInput = session?.user?.phoneNumber.orEmpty(),
                    phoneCallEnabled = session?.user?.phoneCallEnabled == true,
                    startRoute = when {
                        session == null -> MainRoute.Login
                        inviteRequest != null -> MainRoute.Request
                        session.hasCompletedInitialSetup -> MainRoute.Home
                        else -> MainRoute.Terms
                    },
                    checked = todayCheckIn != null,
                    lastCheckedAt = todayCheckIn?.lastCheckedAt.orEmpty(),
                    status = todayCheckIn?.status ?: CheckInStatus.Before,
                    deadline = session?.settings?.deadline ?: it.deadline,
                    selectedDeadline = session?.settings?.deadline ?: it.selectedDeadline,
                    delayMinutes = session?.settings?.delayMinutes ?: it.delayMinutes,
                    pushEnabled = session?.settings?.pushEnabled ?: it.pushEnabled,
                    relationPushEnabled = session?.settings?.relationPushEnabled ?: it.relationPushEnabled,
                    missedPushEnabled = session?.settings?.missedPushEnabled ?: it.missedPushEnabled,
                    guardians = guardians,
                    watchingUsers = watchingUsers,
                    activeInvites = activeInvites,
                    notifications = notifications,
                    inviteRequest = inviteRequest,
                )
            }
        }
    }

    fun refreshSessionAfterLogin(onReady: (MainRoute) -> Unit) {
        viewModelScope.launch {
            val session = getCurrentAuthSessionUseCase()
            val todayCheckIn = session?.user?.id
                ?.let { userId -> runCatching { getTodayCheckInUseCase(userId) }.getOrNull() }
            val guardians = session?.user?.id
                ?.let { userId -> runCatching { getMyGuardiansUseCase(userId) }.getOrNull() }
                .orEmpty()
            val watchingUsers = session?.user?.id
                ?.let { userId -> loadWatchingUsers(userId) }
                .orEmpty()
            val activeInvites = session?.user?.id
                ?.let { userId -> runCatching { getActiveInviteLinksUseCase(userId) }.getOrNull() }
                .orEmpty()
            val notifications = session?.user?.id
                ?.let { userId -> runCatching { getNotificationsUseCase(userId) }.getOrNull() }
                .orEmpty()
            val inviteRequest = _state.value.pendingInviteCode
                ?.let { inviteCode -> runCatching { getGuardianInviteRequestUseCase(inviteCode) }.getOrNull() }
            val route = when {
                session == null -> MainRoute.Login
                inviteRequest != null -> MainRoute.Request
                session.hasCompletedInitialSetup -> MainRoute.Home
                else -> MainRoute.Terms
            }
            update {
                it.copy(
                    sessionChecked = true,
                    currentUser = session?.user,
                    phoneNumberInput = session?.user?.phoneNumber.orEmpty(),
                    phoneCallEnabled = session?.user?.phoneCallEnabled == true,
                    startRoute = route,
                    checked = todayCheckIn != null,
                    lastCheckedAt = todayCheckIn?.lastCheckedAt.orEmpty(),
                    status = todayCheckIn?.status ?: CheckInStatus.Before,
                    deadline = session?.settings?.deadline ?: it.deadline,
                    selectedDeadline = session?.settings?.deadline ?: it.selectedDeadline,
                    delayMinutes = session?.settings?.delayMinutes ?: it.delayMinutes,
                    pushEnabled = session?.settings?.pushEnabled ?: it.pushEnabled,
                    relationPushEnabled = session?.settings?.relationPushEnabled ?: it.relationPushEnabled,
                    missedPushEnabled = session?.settings?.missedPushEnabled ?: it.missedPushEnabled,
                    guardians = guardians,
                    watchingUsers = watchingUsers,
                    activeInvites = activeInvites,
                    notifications = notifications,
                    inviteRequest = inviteRequest,
                )
            }
            onReady(route)
        }
    }

    fun saveInitialSettings(onSaved: () -> Unit) {
        viewModelScope.launch {
            val current = _state.value
            val userId = current.currentUser?.id ?: return@launch
            saveInitialUserSettingsUseCase(
                userId = userId,
                settings = InitialUserSettings(
                    deadline = current.deadline,
                    delayMinutes = current.delayMinutes,
                    pushEnabled = current.pushEnabled,
                    relationPushEnabled = current.relationPushEnabled,
                    missedPushEnabled = current.missedPushEnabled,
                ),
            )
            update { it.copy(startRoute = MainRoute.Home) }
            onSaved()
        }
    }

    private fun saveCurrentSettings() {
        viewModelScope.launch {
            val current = _state.value
            val userId = current.currentUser?.id ?: return@launch
            saveInitialUserSettingsUseCase(
                userId = userId,
                settings = current.toInitialUserSettings(),
            )
        }
    }

    private fun savePhoneContact() {
        viewModelScope.launch {
            val current = _state.value
            val userId = current.currentUser?.id ?: return@launch
            runCatching {
                savePhoneContactUseCase(
                    userId = userId,
                    phoneNumber = current.phoneNumberInput,
                    phoneCallEnabled = current.phoneCallEnabled,
                )
            }.onSuccess { user ->
                update {
                    it.copy(
                        currentUser = user,
                        phoneNumberInput = user.phoneNumber.orEmpty(),
                        phoneCallEnabled = user.phoneCallEnabled,
                    )
                }
                refreshRelations()
            }
        }
    }

    private fun registerPushToken(token: MainPushToken) {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            runCatching {
                registerPushTokenUseCase(
                    userId = userId,
                    registration = PushTokenRegistration(
                        token = token.token,
                        platform = token.platform,
                        deviceId = token.deviceId,
                    ),
                )
            }.onSuccess {
                update { it.copy(registeredPushToken = token) }
            }
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            val notifications = runCatching { getNotificationsUseCase(userId) }.getOrNull().orEmpty()
            update { it.copy(notifications = notifications) }
        }
    }

    private fun sendTestNotification() {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            update {
                it.copy(
                    testNotificationSending = true,
                    testNotificationMessage = null,
                )
            }
            runCatching { sendTestNotificationUseCase(userId) }
                .onSuccess { result ->
                    update {
                        it.copy(
                            testNotificationSending = false,
                            testNotificationMessage = when {
                                result.tokenCount == 0 -> "등록된 푸시 토큰이 없어요. 알림 권한과 FCM 토큰 저장을 확인해 주세요."
                                result.failedCount == 0 -> "테스트 알림을 ${result.sentCount}개 기기에 보냈어요."
                                else -> buildString {
                                    append("테스트 알림 ${result.sentCount}개 성공, ${result.failedCount}개 실패했어요.")
                                    if (result.disabledTokenCount > 0) {
                                        append(" 만료된 토큰 ${result.disabledTokenCount}개를 껐어요.")
                                    }
                                    result.firstError?.let { error ->
                                        append(" 오류: ")
                                        append(error.take(120))
                                    }
                                }
                            },
                        )
                    }
                }
                .onFailure {
                    update {
                        it.copy(
                            testNotificationSending = false,
                            testNotificationMessage = "테스트 알림 전송에 실패했어요.",
                        )
                    }
                }
        }
    }

    private fun selectNotification(notificationId: String) {
        viewModelScope.launch {
            val current = _state.value
            val userId = current.currentUser?.id ?: return@launch
            val notification = current.notifications.firstOrNull { it.id == notificationId }
            runCatching { markNotificationReadUseCase(userId, notificationId) }
            update {
                it.copy(
                    selectedNotificationId = notificationId,
                    selectedNotification = notification,
                    selectedWatchingUserId = notification?.relatedUserId ?: it.selectedWatchingUserId,
                    notifications = it.notifications.filterNot { item -> item.id == notificationId },
                )
            }
            if (notification?.type == LivvingNotificationType.GuardianRequest) {
                val ownerUserId = notification.relatedUserId ?: notification.actorUserId
                val inviteRequest = ownerUserId
                    ?.let { runCatching { getGuardianInviteRequestByOwnerUseCase(it) }.getOrNull() }
                update {
                    it.copy(
                        inviteRequest = inviteRequest,
                        pendingInviteCode = inviteRequest?.inviteCode ?: it.pendingInviteCode,
                    )
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            val current = _state.value
            val userId = current.currentUser?.id
            val token = current.registeredPushToken?.token
            if (userId != null && token != null) {
                runCatching { disablePushTokenUseCase(userId, token) }
            }
            runCatching { logoutUseCase() }
            update {
                MainState(
                    sessionChecked = true,
                    startRoute = MainRoute.Login,
                )
            }
        }
    }

    private fun toggleAndSaveSettings(block: (MainState) -> MainState) {
        update(block)
        saveCurrentSettings()
    }

    fun openInvite(inviteCode: String) {
        viewModelScope.launch {
            val normalizedInviteCode = inviteCode.toInviteCode()
            if (normalizedInviteCode.isBlank()) {
                update { it.copy(manualInviteError = "초대코드를 입력해 주세요.") }
                return@launch
            }
            update { it.copy(pendingInviteCode = normalizedInviteCode) }
            val session = _state.value.currentUser
            if (session == null) {
                update { it.copy(startRoute = MainRoute.Login) }
                return@launch
            }
            val request = runCatching { getGuardianInviteRequestUseCase(normalizedInviteCode) }.getOrNull()
            update {
                it.copy(
                    inviteRequest = request,
                    startRoute = if (request != null) MainRoute.Request else it.startRoute,
                    manualInviteCode = if (request != null) "" else it.manualInviteCode,
                    manualInviteError = if (request == null) "초대코드를 확인할 수 없어요." else null,
                )
            }
        }
    }

    private fun openManualInviteCode() {
        openInvite(_state.value.manualInviteCode)
    }

    private fun acceptInvite() {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            val inviteCode = _state.value.inviteRequest?.inviteCode ?: return@launch
            runCatching { acceptGuardianInviteUseCase(inviteCode, userId) }
                .onSuccess {
                    val guardians = runCatching { getMyGuardiansUseCase(userId) }.getOrNull().orEmpty()
                    val watchingUsers = loadWatchingUsers(userId)
                    val activeInvites = runCatching { getActiveInviteLinksUseCase(userId) }.getOrNull().orEmpty()
                    update {
                        it.copy(
                            guardians = guardians,
                            watchingUsers = watchingUsers,
                            activeInvites = activeInvites,
                            inviteRequest = null,
                            pendingInviteCode = null,
                            relationTab = kr.osj.livving.feature.relations.RelationsTab.Watching,
                        )
                    }
                }
        }
    }

    private suspend fun loadWatchingUsers(userId: String): List<WatchingUser> {
        return runCatching { getWatchingUsersUseCase(userId) }
            .getOrNull()
            .orEmpty()
            .map { watchingUser ->
                val todayCheckIn = runCatching { getTodayCheckInUseCase(watchingUser.id) }.getOrNull()
                watchingUser.copy(
                    status = todayCheckIn?.status ?: CheckInStatus.Before,
                    lastCheckedAt = todayCheckIn?.lastCheckedAt.orEmpty(),
                )
            }
    }

    private inline fun update(block: (MainState) -> MainState) {
        _state.value = block(_state.value)
    }

    private fun MainState.toInitialUserSettings(): InitialUserSettings = InitialUserSettings(
        deadline = deadline,
        delayMinutes = delayMinutes,
        pushEnabled = pushEnabled,
        relationPushEnabled = relationPushEnabled,
        missedPushEnabled = missedPushEnabled,
    )

}

private fun String.toInviteCode(): String {
    val labeledCode = Regex("초대\\s*코드[:：]?\\s*([^\\s]+)")
        .find(this)
        ?.groupValues
        ?.getOrNull(1)
    val token = labeledCode ?: trim()
        .split(Regex("\\s+"))
        .firstOrNull { value -> value.contains("join/") || value.startsWith("livving://") }
        ?: trim()
    val withoutQuery = token
        .substringBefore("?")
        .substringBefore("#")
        .trim()
        .trimEnd('/')
    return when {
        "livving://join/" in withoutQuery -> withoutQuery.substringAfter("livving://join/")
        "/join/" in withoutQuery -> withoutQuery.substringAfterLast("/join/")
        else -> withoutQuery.substringAfterLast("/")
    }.trim()
}
