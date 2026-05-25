package kr.osj.livving.feature.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.LivvingTheme
import kr.osj.livving.core.ui.LivvingTone

@Preview
@Composable
private fun NotificationsScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            NotificationsScreen(
                pushEnabled = true,
                relationPushEnabled = true,
                missedPushEnabled = true,
                notifications = listOf(
                    NotificationUiModel(
                        id = "1",
                        title = "오승준님 안부 미확인",
                        time = "08:35",
                        desc = "08:30까지 안부 확인이 없었어요.",
                        tone = LivvingTone.Red,
                        read = false,
                        opensAlert = true,
                        opensRequest = false,
                    ),
                ),
                onNotificationClick = {},
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
                message = "08:30까지 안부 확인이 없었어요.",
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
