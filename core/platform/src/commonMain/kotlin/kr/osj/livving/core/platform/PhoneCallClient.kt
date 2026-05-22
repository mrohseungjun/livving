package kr.osj.livving.core.platform

interface PhoneCallClient {
    fun call(phoneNumber: String)
}

expect fun platformPhoneCallClient(): PhoneCallClient
