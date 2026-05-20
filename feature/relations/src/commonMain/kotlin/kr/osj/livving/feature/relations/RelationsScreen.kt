package kr.osj.livving.feature.relations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingAvatar
import kr.osj.livving.core.ui.LivvingBadge
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingCoral
import kr.osj.livving.core.ui.LivvingDanger
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingSegmented
import kr.osj.livving.core.ui.LivvingSuccess
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.LivvingWarning
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RelationsScreen(
    selectedTab: RelationsTab,
    guardians: List<RelationGuardianUiModel>,
    watchingUsers: List<WatchingUserUiModel>,
    deadline: String,
    alertAt: String,
    onMyGuardiansClick: () -> Unit,
    onWatchingClick: () -> Unit,
    onGuardianClick: (RelationGuardianUiModel) -> Unit,
    onInviteClick: () -> Unit,
    onWatchingUserClick: (WatchingUserUiModel) -> Unit,
    viewModel: RelationsViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "관계",
        sub = "내 보호자와 내가 보호 중인 사람을 관리해요.",
    )
    LivvingSegmented(
        left = "내 보호자",
        right = "내가 보호 중",
        selectedLeft = selectedTab == RelationsTab.MyGuardians,
        onLeft = onMyGuardiansClick,
        onRight = onWatchingClick,
    )
    Spacer(Modifier.height(24.dp))
    if (selectedTab == RelationsTab.MyGuardians) {
        guardians.forEachIndexed { index, guardian ->
            GuardianRow(
                guardian = guardian,
                tone = listOf(LivvingTone.Coral, LivvingTone.Green, LivvingTone.Orange).getOrElse(index) { LivvingTone.Purple },
                onClick = { onGuardianClick(guardian) },
            )
            Spacer(Modifier.height(14.dp))
        }
        Spacer(Modifier.height(12.dp))
        LivvingPrimaryButton(
            text = "보호자 초대하기",
            onClick = onInviteClick,
        )
        Spacer(Modifier.height(14.dp))
        LivvingInfoBox(
            text = "보호자 그룹 링크를 한 번 공유하고, 각 보호자가 앱에서 수락하면 연결됩니다.",
            tone = LivvingTone.Neutral,
        )
    } else {
        watchingUsers.forEach { user ->
            WatchingRow(
                user = user,
                deadline = deadline,
                alertAt = alertAt,
                onClick = { onWatchingUserClick(user) },
            )
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
private fun GuardianRow(
    guardian: RelationGuardianUiModel,
    tone: LivvingTone,
    onClick: () -> Unit,
) {
    LivvingCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LivvingAvatar(guardian.name, tone = tone)
            Column(Modifier.weight(1f)) {
                Text(guardian.name, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(guardian.relation, color = LivvingMuted)
            }
            when (guardian.status) {
                RelationGuardianStatus.Accepted -> LivvingBadge("연결됨", LivvingTone.Green)
                RelationGuardianStatus.Pending -> LivvingBadge("초대 대기", LivvingTone.Orange)
            }
        }
    }
}

@Composable
private fun WatchingRow(
    user: WatchingUserUiModel,
    deadline: String,
    alertAt: String,
    onClick: () -> Unit,
) {
    val tone = when (user.state) {
        WatchingState.Safe -> LivvingTone.Green
        WatchingState.Waiting -> LivvingTone.Orange
        WatchingState.Missed -> LivvingTone.Coral
    }
    val color = when (user.state) {
        WatchingState.Safe -> LivvingSuccess
        WatchingState.Waiting -> LivvingWarning
        WatchingState.Missed -> LivvingDanger
    }
    LivvingCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            LivvingAvatar(user.name, tone = tone)
            Column(Modifier.weight(1f)) {
                Text(user.name, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(user.text, color = color, fontWeight = FontWeight.Bold)
                Text("마감 $deadline · 알림 $alertAt", color = Color(0xFFA3A3A3), fontSize = 12.sp)
            }
            Text("전화", color = LivvingCoral, fontWeight = FontWeight.Bold)
        }
    }
}
