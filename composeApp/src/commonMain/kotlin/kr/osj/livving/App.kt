package kr.osj.livving

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingTheme
import kr.osj.livving.di.appModule
import kr.osj.livving.feature.greeting.GreetingRoute
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App() {
    KoinApplication(configuration = koinConfiguration {
        modules(appModule)
    }) {
        LivvingTheme {
            GreetingRoute()
        }
    }
}
