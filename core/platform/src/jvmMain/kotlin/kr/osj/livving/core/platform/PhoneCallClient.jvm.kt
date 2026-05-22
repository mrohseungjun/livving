package kr.osj.livving.core.platform

actual fun platformPhoneCallClient(): PhoneCallClient = JvmPhoneCallClient

private object JvmPhoneCallClient : PhoneCallClient {
    override fun call(phoneNumber: String) = Unit
}
