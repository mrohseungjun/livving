package kr.osj.livving.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.InitialUserSettings
import kr.osj.livving.domain.livving.usecase.CompleteCheckInUseCase
import kr.osj.livving.domain.livving.usecase.CreateGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.GetCurrentAuthSessionUseCase
import kr.osj.livving.domain.livving.usecase.GetTodayCheckInUseCase
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
            is MainIntent.SaveDelay -> Unit
            is MainIntent.ToggleTerms -> toggleTerms(intent.item)
            MainIntent.ToggleAllTerms -> toggleAllTerms()
            is MainIntent.SelectRelationTab -> update { it.copy(relationTab = intent.tab) }
            MainIntent.CreateInvite -> createInvite()
            MainIntent.TogglePush -> update { it.copy(pushEnabled = !it.pushEnabled) }
            MainIntent.ToggleRelationPush -> update { it.copy(relationPushEnabled = !it.relationPushEnabled) }
            MainIntent.ToggleMissedPush -> update { it.copy(missedPushEnabled = !it.missedPushEnabled) }
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
                .onSuccess { guardian ->
                    update {
                        it.copy(guardians = it.guardians + guardian)
                    }
                }
        }
    }

    private fun loadSession() {
        viewModelScope.launch {
            val session = getCurrentAuthSessionUseCase()
            val todayCheckIn = session?.user?.id
                ?.let { userId -> runCatching { getTodayCheckInUseCase(userId) }.getOrNull() }
            update {
                it.copy(
                    sessionChecked = true,
                    currentUser = session?.user,
                    startRoute = when {
                        session == null -> MainRoute.Login
                        session.hasCompletedInitialSetup -> MainRoute.Home
                        else -> MainRoute.Terms
                    },
                    checked = todayCheckIn != null,
                    lastCheckedAt = todayCheckIn?.lastCheckedAt.orEmpty(),
                    status = todayCheckIn?.status ?: CheckInStatus.Before,
                )
            }
        }
    }

    fun refreshSessionAfterLogin(onReady: (MainRoute) -> Unit) {
        viewModelScope.launch {
            val session = getCurrentAuthSessionUseCase()
            val todayCheckIn = session?.user?.id
                ?.let { userId -> runCatching { getTodayCheckInUseCase(userId) }.getOrNull() }
            val route = when {
                session == null -> MainRoute.Login
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

    private inline fun update(block: (MainState) -> MainState) {
        _state.value = block(_state.value)
    }

}
