package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.KakaoLoginResult
import kr.osj.livving.domain.livving.KakaoLoginToken

interface AuthRepository {
    suspend fun loginWithKakao(token: KakaoLoginToken): KakaoLoginResult
    suspend fun logout()
}
