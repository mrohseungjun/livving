package kr.osj.livving.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingTheme

@Preview
@Composable
private fun SplashScreenPreview() {
    LivvingTheme {
        SplashScreen(
            canFinish = false,
            onFinished = {},
            viewModel = SplashViewModel(),
        )
    }
}
