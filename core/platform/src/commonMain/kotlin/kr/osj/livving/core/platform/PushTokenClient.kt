package kr.osj.livving.core.platform

data class PlatformPushToken(
    val token: String,
    val platform: String,
    val deviceId: String? = null,
)

interface PushTokenClient {
    suspend fun getToken(): PlatformPushToken?
}

expect fun platformPushTokenClient(): PushTokenClient
