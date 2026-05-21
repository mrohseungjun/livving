package kr.osj.livving.auth

import kr.osj.livving.domain.livving.KakaoLoginToken
import kr.osj.livving.domain.livving.repository.KakaoAuthClient

class IosKakaoAuthClient : KakaoAuthClient {
    override suspend fun login(): KakaoLoginToken {
        throw UnsupportedOperationException("iOS Kakao SDK 연결이 아직 필요합니다.")
    }

    override suspend fun logout() = Unit
}
