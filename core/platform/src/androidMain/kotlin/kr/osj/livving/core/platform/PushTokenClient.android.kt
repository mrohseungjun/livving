package kr.osj.livving.core.platform

import android.provider.Settings
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

actual fun platformPushTokenClient(): PushTokenClient = AndroidPushTokenClient

private object AndroidPushTokenClient : PushTokenClient {
    override suspend fun getToken(): PlatformPushToken? {
        val token = suspendCancellableCoroutine<String?> { continuation ->
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { value ->
                    livvingLogD(
                        tag = "LivvingPushToken",
                        message = "fcm token fetched tokenPrefix=${value.take(12)}",
                    )
                    continuation.resume(value)
                }
                .addOnFailureListener { throwable ->
                    livvingLogD(
                        tag = "LivvingPushToken",
                        message = "fcm token fetch failed ${throwable::class.simpleName}: ${throwable.message}",
                    )
                    continuation.resume(null)
                }
        } ?: return null

        val context = AndroidTextShareContext.requireContext()
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return PlatformPushToken(
            token = token,
            platform = "android",
            deviceId = deviceId,
        )
    }
}
