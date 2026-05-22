package kr.osj.livving.core.platform

actual fun platformPushTokenClient(): PushTokenClient = IosPushTokenClient

private object IosPushTokenClient : PushTokenClient {
    override suspend fun getToken(): PlatformPushToken? = null
}
