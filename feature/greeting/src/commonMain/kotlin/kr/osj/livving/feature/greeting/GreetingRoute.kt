package kr.osj.livving.feature.greeting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject

@Composable
fun GreetingRoute(
    viewModel: GreetingViewModel = koinInject(),
) {
    val state by viewModel.state.collectAsState()

    GreetingScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
