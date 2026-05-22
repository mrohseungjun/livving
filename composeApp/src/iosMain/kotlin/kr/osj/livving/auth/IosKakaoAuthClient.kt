package kr.osj.livving.auth

import kr.osj.livving.domain.livving.KakaoLoginToken
import kr.osj.livving.domain.livving.repository.KakaoAuthClient

class IosKakaoAuthClient : KakaoAuthClient {
    override suspend fun login(): KakaoLoginToken {
        return requireNotNull(iosKakaoAuthClient) {
            "iOS Kakao SDK client must be installed before starting Koin."
        }.login()
    }

    override suspend fun logout() {
        iosKakaoAuthClient?.logout()
    }
}

private var iosKakaoAuthClient: KakaoAuthClient? = null

fun installIosKakaoAuthClient(client: KakaoAuthClient) {
    iosKakaoAuthClient = client
}
