package kr.osj.livving

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kakao.sdk.common.KakaoSdk
import kr.osj.livving.auth.AndroidKakaoAuthContext
import kr.osj.livving.core.platform.AndroidTextShareContext

class MainActivity : ComponentActivity() {
    private var inviteCode by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        requestNotificationPermissionIfNeeded()
        AndroidKakaoAuthContext.initialize(this)
        AndroidTextShareContext.initialize(this)
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
        inviteCode = intent.extractInviteCode()

        setContent {
            App(initialInviteCode = inviteCode)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        inviteCode = intent.extractInviteCode()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            getString(R.string.livving_notification_channel_id),
            getString(R.string.livving_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
    }

    private companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }
}

private fun Intent?.extractInviteCode(): String? {
    val uri = this?.data ?: return null
    return when {
        uri.scheme == "https" && uri.host == "livving.app" && uri.pathSegments.firstOrNull() == "join" -> {
            uri.pathSegments.getOrNull(1)
        }
        uri.scheme == "livving" && uri.host == "join" -> {
            uri.pathSegments.firstOrNull()
        }
        else -> null
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
