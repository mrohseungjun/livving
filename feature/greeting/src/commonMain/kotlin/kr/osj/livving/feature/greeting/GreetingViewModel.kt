package kr.osj.livving.feature.greeting

import kr.osj.livving.domain.greeting.GetGreetingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GreetingViewModel(
    private val getGreetingUseCase: GetGreetingUseCase,
) {
    private val _state = MutableStateFlow(GreetingState())
    val state: StateFlow<GreetingState> = _state.asStateFlow()

    fun onIntent(intent: GreetingIntent) {
        when (intent) {
            GreetingIntent.ToggleContent -> toggleContent()
        }
    }

    private fun toggleContent() {
        val currentState = _state.value
        val willShowContent = !currentState.isContentVisible

        _state.value = currentState.copy(
            isContentVisible = willShowContent,
            greeting = if (willShowContent) getGreetingUseCase() else currentState.greeting,
        )
    }
}
