package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.KakaoLoginToken

interface KakaoAuthClient {
    suspend fun login(): KakaoLoginToken
    suspend fun logout()
}
