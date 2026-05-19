package kr.osj.livving.feature.greeting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun GreetingRoute(
    viewModel: GreetingViewModel = remember { GreetingViewModel() },
) {
    val state by viewModel.state.collectAsState()

    GreetingScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
