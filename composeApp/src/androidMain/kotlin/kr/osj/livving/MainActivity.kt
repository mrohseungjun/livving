package kr.osj.livving

import android.content.Intent
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
