package kr.osj.livving.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kr.osj.livving.domain.livving.LivvingUser
import kr.osj.livving.domain.livving.usecase.LoginWithKakaoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val loading: Boolean = false,
    val user: LivvingUser? = null,
    val errorMessage: String? = null,
)

sealed interface LoginIntent {
    data object ClickKakao : LoginIntent
    data object ClearError : LoginIntent
}

class LoginViewModel(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.ClickKakao -> loginWithKakao()
            LoginIntent.ClearError -> _state.update { it.copy(errorMessage = null) }
        }
    }

    private fun loginWithKakao() {
        if (_state.value.loading) return

        viewModelScope.launch {
            _state.update { it.copy(loading = true, errorMessage = null) }
            runCatching { loginWithKakaoUseCase() }
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            loading = false,
                            user = result.user,
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            loading = false,
                            errorMessage = throwable.message ?: "카카오 로그인에 실패했어요.",
                        )
                    }
                }
        }
    }
}

class TermsViewModel : ViewModel()
