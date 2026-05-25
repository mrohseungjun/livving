package kr.osj.livving.feature.relations

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kr.osj.livving.core.ui.LivvingScrollableScreen
import kr.osj.livving.core.ui.LivvingTheme

@Preview
@Composable
private fun RelationsMyGuardiansScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            RelationsScreen(
                selectedTab = RelationsTab.MyGuardians,
                guardians = previewGuardians,
                watchingUsers = previewWatchingUsers,
                deadline = "08:30",
                alertAt = "08:35",
                manualInviteCode = "7f25ea22fc136b6c",
                manualInviteError = null,
                onMyGuardiansClick = {},
                onWatchingClick = {},
                onGuardianClick = {},
                onManualInviteCodeChange = {},
                onManualInviteSubmit = {},
                onWatchingUserClick = {},
                onWatchingCallClick = {},
                viewModel = RelationsViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun RelationsWatchingScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            RelationsScreen(
                selectedTab = RelationsTab.Watching,
                guardians = previewGuardians,
                watchingUsers = previewWatchingUsers,
                deadline = "08:30",
                alertAt = "08:35",
                manualInviteCode = "",
                manualInviteError = null,
                onMyGuardiansClick = {},
                onWatchingClick = {},
                onGuardianClick = {},
                onManualInviteCodeChange = {},
                onManualInviteSubmit = {},
                onWatchingUserClick = {},
                onWatchingCallClick = {},
                viewModel = RelationsViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun InviteScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            InviteScreen(
                inviteCode = "7f25ea22fc136b6c",
                inviteLink = "livving://join/7f25ea22fc136b6c",
                pendingCount = 1,
                acceptedCount = 2,
                onBackClick = {},
                onCreateInviteClick = {},
                onCopyInviteClick = {},
                onShareInviteClick = {},
                viewModel = InviteViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun InviteStatusScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            InviteStatusScreen(
                onBackClick = {},
                viewModel = InviteStatusViewModel(),
            )
        }
    }
}

@Preview
@Composable
private fun GuardianDetailScreenPreview() {
    LivvingTheme {
        LivvingScrollableScreen {
            GuardianDetailScreen(
                name = "김지연",
                relation = "딸",
                connected = true,
                phoneNumber = "01012345678",
                deadline = "08:30",
                delayMinutes = 5,
                onBackClick = {},
                onCallClick = {},
                onDisconnectClick = {},
                viewModel = GuardianDetailViewModel(),
            )
        }
    }
}

private val previewGuardians = listOf(
    RelationGuardianUiModel(1, "김지연", "딸", RelationGuardianStatus.Accepted, "01012345678"),
    RelationGuardianUiModel(2, "이민호", "친구", RelationGuardianStatus.Accepted),
    RelationGuardianUiModel(3, "박선영", "이웃", RelationGuardianStatus.Pending),
)

private val previewWatchingUsers = listOf(
    WatchingUserUiModel(1, "preview-safe", "오승준", WatchingState.Safe, "오늘 안부 확인 완료", "5월 22일 08:25", "01012345678"),
    WatchingUserUiModel(2, "preview-waiting", "김영희", WatchingState.Waiting, "아직 안부 확인 전", "마감 대기 중"),
    WatchingUserUiModel(3, "preview-missed", "박민수", WatchingState.Missed, "안부 미확인", "보호자 알림 대상", "01022223333"),
)
