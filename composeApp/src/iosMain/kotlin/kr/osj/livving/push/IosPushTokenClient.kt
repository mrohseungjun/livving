package kr.osj.livving.push

import kr.osj.livving.core.platform.PlatformPushToken
import kr.osj.livving.core.platform.PushTokenClient
import kr.osj.livving.core.platform.installIosPushTokenClient

interface IosPushTokenProvider {
    suspend fun getToken(): PlatformPushToken?
}

fun installIosPushTokenProvider(provider: IosPushTokenProvider) {
    installIosPushTokenClient(
        object : PushTokenClient {
            override suspend fun getToken(): PlatformPushToken? = provider.getToken()
        },
    )
}
