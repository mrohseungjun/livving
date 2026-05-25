package kr.osj.livving.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.LivvingTheme

@Preview
@Composable
private fun SettingsScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            SettingsScreen(
                userName = "오승준",
                deadline = "08:30",
                delayMinutes = 5,
                pushEnabled = true,
                relationPushEnabled = true,
                missedPushEnabled = true,
                hasRegisteredPushToken = true,
                pushTokenRegistering = false,
                pushTokenMessage = null,
                settingsSaving = false,
                settingsMessage = "설정이 저장됐어요.",
                testNotificationSending = false,
                testNotificationMessage = "테스트 알림을 2개 기기에 보냈어요.",
                onDeadlineClick = {},
                onDelayClick = {},
                onScheduleClick = {},
                onPushToggleClick = {},
                onRelationPushToggleClick = {},
                onMissedPushToggleClick = {},
                onSendTestNotificationClick = {},
                onRetryPushTokenClick = {},
                onProfileClick = {},
                onPrivacyClick = {},
                onLogoutClick = {},
                viewModel = SettingsViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun ScheduleScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            ScheduleScreen(
                deadline = "08:30",
                delayMinutes = 5,
                onBackClick = {},
                viewModel = ScheduleViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            ProfileScreen(
                userName = "오승준",
                guardianCount = 2,
                watchingCount = 3,
                phoneNumber = "01012345678",
                phoneCallEnabled = true,
                onBackClick = {},
                onPhoneNumberChange = {},
                onPhoneCallEnabledToggle = {},
                onPhoneSaveClick = {},
                viewModel = ProfileViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun PrivacyScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            PrivacyScreen(
                onBackClick = {},
                viewModel = PrivacyViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            HistoryScreen(
                onBackClick = {},
                viewModel = HistoryViewModel(),
            )
        }
    }
}
