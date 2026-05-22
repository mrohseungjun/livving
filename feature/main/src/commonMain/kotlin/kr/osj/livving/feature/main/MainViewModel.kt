package kr.osj.livving.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.InitialUserSettings
import kr.osj.livving.domain.livving.WatchingUser
import kr.osj.livving.domain.livving.usecase.AcceptGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.CompleteCheckInUseCase
import kr.osj.livving.domain.livving.usecase.CreateGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.GetActiveInviteLinksUseCase
import kr.osj.livving.domain.livving.usecase.GetCurrentAuthSessionUseCase
import kr.osj.livving.domain.livving.usecase.GetGuardianInviteRequestUseCase
import kr.osj.livving.domain.livving.usecase.GetMyGuardiansUseCase
import kr.osj.livving.domain.livving.usecase.GetTodayCheckInUseCase
import kr.osj.livving.domain.livving.usecase.GetWatchingUsersUseCase
import kr.osj.livving.domain.livving.usecase.SaveInitialUserSettingsUseCase
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
    private val acceptGuardianInviteUseCase: AcceptGuardianInviteUseCase,
    private val getWatchingUsersUseCase: GetWatchingUsersUseCase,
    private val getCurrentAuthSessionUseCase: GetCurrentAuthSessionUseCase,
    private val getTodayCheckInUseCase: GetTodayCheckInUseCase,
    private val saveInitialUserSettingsUseCase: SaveInitialUserSettingsUseCase,
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
            MainIntent.CreateInvite -> createInvite()
            MainIntent.AcceptInvite -> acceptInvite()
            MainIntent.RejectInvite -> update { it.copy(inviteRequest = null, pendingInviteCode = null) }
            MainIntent.TogglePush -> toggleAndSaveSettings { it.copy(pushEnabled = !it.pushEnabled) }
            MainIntent.ToggleRelationPush -> toggleAndSaveSettings { it.copy(relationPushEnabled = !it.relationPushEnabled) }
            MainIntent.ToggleMissedPush -> toggleAndSaveSettings { it.copy(missedPushEnabled = !it.missedPushEnabled) }
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
            val inviteRequest = _state.value.pendingInviteCode
                ?.let { inviteCode -> runCatching { getGuardianInviteRequestUseCase(inviteCode) }.getOrNull() }
            update {
                it.copy(
                    sessionChecked = true,
                    currentUser = session?.user,
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

    private fun toggleAndSaveSettings(block: (MainState) -> MainState) {
        update(block)
        saveCurrentSettings()
    }

    fun openInvite(inviteCode: String) {
        viewModelScope.launch {
            update { it.copy(pendingInviteCode = inviteCode) }
            val session = _state.value.currentUser
            if (session == null) {
                update { it.copy(startRoute = MainRoute.Login) }
                return@launch
            }
            val request = runCatching { getGuardianInviteRequestUseCase(inviteCode) }.getOrNull()
            update {
                it.copy(
                    inviteRequest = request,
                    startRoute = if (request != null) MainRoute.Request else it.startRoute,
                )
            }
        }
    }

    private fun acceptInvite() {
        viewModelScope.launch {
            val userId = _state.value.currentUser?.id ?: return@launch
            val inviteCode = _state.value.inviteRequest?.inviteCode ?: return@launch
            runCatching { acceptGuardianInviteUseCase(inviteCode, userId) }
                .onSuccess {
                    val watchingUsers = loadWatchingUsers(userId)
                    update {
                        it.copy(
                            watchingUsers = watchingUsers,
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
