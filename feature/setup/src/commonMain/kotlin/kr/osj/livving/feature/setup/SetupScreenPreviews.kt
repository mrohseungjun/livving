package kr.osj.livving.feature.setup

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.LivvingTheme

@Preview
@Composable
private fun DeadlineScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            DeadlineScreen(
                selectedDeadline = "08:30",
                fromSettings = false,
                onBackClick = {},
                onDeadlineClick = {},
                onSaveClick = {},
                viewModel = DeadlineViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun DelayScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            DelayScreen(
                deadline = "08:30",
                delayMinutes = 5,
                fromSettings = false,
                onBackClick = {},
                onDelayClick = {},
                onSaveClick = {},
                viewModel = DelayViewModel(),
            )
        }
    }
}
