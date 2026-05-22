package kr.osj.livving.core.platform

import android.content.Intent
import android.net.Uri

actual fun platformPhoneCallClient(): PhoneCallClient = AndroidPhoneCallClient

private object AndroidPhoneCallClient : PhoneCallClient {
    override fun call(phoneNumber: String) {
        val context = AndroidTextShareContext.requireContext()
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
