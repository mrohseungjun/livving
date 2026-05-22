package kr.osj.livving

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.platform.platformTextShareClient
import kr.osj.livving.core.ui.LivvingTheme
import kr.osj.livving.di.appModule
import kr.osj.livving.feature.main.MainRoute
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App(initialInviteCode: String? = null) {
    KoinApplication(configuration = koinConfiguration {
        modules(appModule)
    }) {
        val shareClient = remember { platformTextShareClient() }
        LivvingTheme {
            MainRoute(
                initialInviteCode = initialInviteCode,
                onShareText = shareClient::shareText,
            )
        }
    }
}
