package kr.osj.livving.feature.main

import androidx.lifecycle.ViewModel
import kr.osj.livving.domain.livving.usecase.CompleteCheckInUseCase
import kr.osj.livving.domain.livving.usecase.CreateGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.ToggleLateCheckInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    private val completeCheckInUseCase: CompleteCheckInUseCase,
    private val toggleLateCheckInUseCase: ToggleLateCheckInUseCase,
    private val createGuardianInviteUseCase: CreateGuardianInviteUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Navigate -> navigate(intent.route)
            MainIntent.CompleteCheckIn -> completeCheckIn()
            MainIntent.ToggleLateState -> toggleLateState()
            is MainIntent.SelectDeadline -> update { it.copy(selectedDeadline = intent.time) }
            is MainIntent.SaveDeadline -> saveDeadline(intent.fromSettings)
            is MainIntent.SelectDelay -> update { it.copy(delayMinutes = intent.minutes) }
            is MainIntent.SaveDelay -> navigate(if (intent.fromSettings) MainRoute.Settings else MainRoute.Home)
            is MainIntent.ToggleTerms -> toggleTerms(intent.item)
            MainIntent.ToggleAllTerms -> toggleAllTerms()
            is MainIntent.SelectRelationTab -> update { it.copy(relationTab = intent.tab) }
            MainIntent.CreateInvite -> createInvite()
            MainIntent.TogglePush -> update { it.copy(pushEnabled = !it.pushEnabled) }
            MainIntent.ToggleRelationPush -> update { it.copy(relationPushEnabled = !it.relationPushEnabled) }
            MainIntent.ToggleMissedPush -> update { it.copy(missedPushEnabled = !it.missedPushEnabled) }
        }
    }

    private fun navigate(route: MainRoute) {
        update {
            it.copy(
                route = route,
                selectedDeadline = if (route == MainRoute.SetupDeadline || route == MainRoute.DeadlineChange) {
                    it.deadline
                } else {
                    it.selectedDeadline
                },
            )
        }
    }

    private fun completeCheckIn() {
        val completion = completeCheckInUseCase()
        update {
            it.copy(
                checked = true,
                lastCheckedAt = completion.lastCheckedAt,
                status = completion.status,
            )
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
                route = if (fromSettings) MainRoute.Settings else MainRoute.SetupDelay,
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
        update {
            it.copy(guardians = createGuardianInviteUseCase(it.guardians))
        }
    }

    private inline fun update(block: (MainState) -> MainState) {
        _state.value = block(_state.value)
    }
}
