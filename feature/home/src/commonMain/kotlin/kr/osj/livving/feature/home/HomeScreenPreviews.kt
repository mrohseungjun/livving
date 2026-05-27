package kr.osj.livving.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.LivvingTheme

@Preview
@Composable
private fun HomeScreenCheckedPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            HomeScreen(
                deadline = "08:30",
                delayMinutes = 5,
                checked = true,
                lastCheckedAt = "5월 22일 19:35",
                late = false,
                acceptedGuardianNames = listOf("김지연", "이민호"),
                onCheckInClick = {},
                onRelationsClick = {},
                onHistoryClick = {},
                onToggleLateClick = {},
                viewModel = HomeViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenLatePreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            HomeScreen(
                deadline = "08:30",
                delayMinutes = 5,
                checked = false,
                lastCheckedAt = "",
                late = true,
                acceptedGuardianNames = listOf("김지연"),
                onCheckInClick = {},
                onRelationsClick = {},
                onHistoryClick = {},
                onToggleLateClick = {},
                viewModel = HomeViewModel(),
            )
        }
    }
}
