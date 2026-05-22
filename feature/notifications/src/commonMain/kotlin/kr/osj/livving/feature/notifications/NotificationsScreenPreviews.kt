package kr.osj.livving.feature.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.LivvingTheme

@Preview
@Composable
private fun NotificationsScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            NotificationsScreen(
                deadline = "08:30",
                alertAt = "08:35",
                onAlertClick = {},
                onRequestClick = {},
                viewModel = NotificationsViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun AlertScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            AlertScreen(
                userName = "오승준",
                lastCheckedAt = "5월 22일 08:10",
                phoneNumber = "01012345678",
                deadline = "08:30",
                alertAt = "08:35",
                onBackClick = {},
                onCallClick = {},
                onConfirmClick = {},
                viewModel = AlertViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun RequestScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            RequestScreen(
                ownerName = "박선영",
                onBackClick = {},
                onAcceptClick = {},
                onRejectClick = {},
                viewModel = RequestViewModel(),
            )
        }
    }
}
