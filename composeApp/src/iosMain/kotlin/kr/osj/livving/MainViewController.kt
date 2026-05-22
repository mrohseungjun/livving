package kr.osj.livving

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController(initialInviteCode: String? = null) = ComposeUIViewController {
    App(initialInviteCode = initialInviteCode)
}
