package kr.osj.livving.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private var started = false

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.Start -> start()
        }
    }

    private fun start() {
        if (started) return
        started = true

        viewModelScope.launch {
            delay(SPLASH_DURATION_MILLIS)
            _state.update { it.copy(isFinished = true) }
        }
    }

    private companion object {
        const val SPLASH_DURATION_MILLIS = 1_200L
    }
}
