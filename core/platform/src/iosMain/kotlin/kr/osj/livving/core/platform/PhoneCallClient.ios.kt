package kr.osj.livving.core.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun platformPhoneCallClient(): PhoneCallClient = IosPhoneCallClient

private object IosPhoneCallClient : PhoneCallClient {
    override fun call(phoneNumber: String) {
        val url = NSURL(string = "tel://$phoneNumber")
        UIApplication.sharedApplication.openURL(url)
    }
}
