package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.KakaoLoginResult
import kr.osj.livving.domain.livving.repository.AuthRepository
import kr.osj.livving.domain.livving.repository.KakaoAuthClient

class LoginWithKakaoUseCase(
    private val kakaoAuthClient: KakaoAuthClient,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): KakaoLoginResult {
        val token = kakaoAuthClient.login()
        return authRepository.loginWithKakao(token)
    }
}
