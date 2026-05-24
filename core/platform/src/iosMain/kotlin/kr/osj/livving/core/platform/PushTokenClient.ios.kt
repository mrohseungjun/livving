package kr.osj.livving.core.platform

actual fun platformPushTokenClient(): PushTokenClient = IosPushTokenClient

private object IosPushTokenClient : PushTokenClient {
    override suspend fun getToken(): PlatformPushToken? = iosPushTokenClient?.getToken()
}

private var iosPushTokenClient: PushTokenClient? = null

fun installIosPushTokenClient(client: PushTokenClient) {
    iosPushTokenClient = client
}
