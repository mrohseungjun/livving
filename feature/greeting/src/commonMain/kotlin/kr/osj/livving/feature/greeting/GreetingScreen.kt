package kr.osj.livving.feature.greeting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kr.osj.livving.core.ui.LivvingBodyText
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingScreen

@Composable
fun GreetingScreen(
    state: GreetingState,
    onIntent: (GreetingIntent) -> Unit,
) {
    LivvingScreen {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LivvingPrimaryButton(
                text = "Click me!",
                onClick = { onIntent(GreetingIntent.ToggleContent) },
            )
            AnimatedVisibility(state.isContentVisible) {
                LivvingBodyText("Compose: ${state.greeting}")
            }
        }
    }
}
