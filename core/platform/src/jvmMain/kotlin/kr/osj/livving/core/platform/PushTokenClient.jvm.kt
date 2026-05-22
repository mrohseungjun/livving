package kr.osj.livving.core.platform

actual fun platformPushTokenClient(): PushTokenClient = JvmPushTokenClient

private object JvmPushTokenClient : PushTokenClient {
    override suspend fun getToken(): PlatformPushToken? = null
}
