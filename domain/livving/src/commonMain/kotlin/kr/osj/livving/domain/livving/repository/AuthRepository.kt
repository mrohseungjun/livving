package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.AuthSession
import kr.osj.livving.domain.livving.InitialUserSettings
import kr.osj.livving.domain.livving.KakaoLoginResult
import kr.osj.livving.domain.livving.KakaoLoginToken

interface AuthRepository {
    suspend fun getCurrentSession(): AuthSession?
    suspend fun loginWithKakao(token: KakaoLoginToken): KakaoLoginResult
    suspend fun saveInitialSettings(userId: String, settings: InitialUserSettings)
    suspend fun logout()
}
