package kr.osj.livving.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.LivvingTheme
import kr.osj.livving.domain.livving.AuthSession
import kr.osj.livving.domain.livving.InitialUserSettings
import kr.osj.livving.domain.livving.KakaoLoginResult
import kr.osj.livving.domain.livving.KakaoLoginToken
import kr.osj.livving.domain.livving.LivvingUser
import kr.osj.livving.domain.livving.repository.AuthRepository
import kr.osj.livving.domain.livving.repository.KakaoAuthClient
import kr.osj.livving.domain.livving.usecase.LoginWithKakaoUseCase

@Preview
@Composable
private fun LoginScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            LoginScreen(
                onLoginSuccess = {},
                viewModel = LoginViewModel(
                    LoginWithKakaoUseCase(
                        kakaoAuthClient = PreviewKakaoAuthClient,
                        authRepository = PreviewAuthRepository,
                    ),
                ),
            )
        }
    }
}

@Preview
@Composable
private fun TermsScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            TermsScreen(
                state = AuthTermsState(service = true, privacy = true, age = true, marketing = false),
                onBackClick = {},
                onToggleAllClick = {},
                onToggleServiceClick = {},
                onTogglePrivacyClick = {},
                onToggleAgeClick = {},
                onToggleMarketingClick = {},
                onContinueClick = {},
                viewModel = TermsViewModel(),
            )
        }
    }
}

private object PreviewKakaoAuthClient : KakaoAuthClient {
    override suspend fun login(): KakaoLoginToken = KakaoLoginToken(
        accessToken = "preview-access-token",
        idToken = "preview-id-token",
    )

    override suspend fun logout() = Unit
}

private object PreviewAuthRepository : AuthRepository {
    private val user = LivvingUser(
        id = "preview-user",
        kakaoId = "preview-kakao",
        nickname = "오승준",
        profileImageUrl = null,
    )

    override suspend fun getCurrentSession(): AuthSession? = AuthSession(
        user = user,
        hasCompletedInitialSetup = true,
        settings = null,
    )

    override suspend fun loginWithKakao(token: KakaoLoginToken): KakaoLoginResult {
        return KakaoLoginResult(user = user, isNewUser = false)
    }

    override suspend fun saveInitialSettings(userId: String, settings: InitialUserSettings) = Unit
    override suspend fun savePhoneContact(userId: String, phoneNumber: String?, phoneCallEnabled: Boolean): LivvingUser = user.copy(
        phoneNumber = phoneNumber,
        phoneCallEnabled = phoneCallEnabled,
    )
    override suspend fun logout() = Unit
}
