package kr.osj.livving

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingTheme
import kr.osj.livving.feature.greeting.GreetingRoute

@Composable
@Preview
fun App() {
    LivvingTheme {
        GreetingRoute()
    }
}
