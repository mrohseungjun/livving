package kr.osj.livving.feature.home

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingAvatar
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingCenterText
import kr.osj.livving.core.ui.LivvingCheckInButton
import kr.osj.livving.core.ui.LivvingCoral
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingIconCircle
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingPurple
import kr.osj.livving.core.ui.LivvingSecondaryButton
import kr.osj.livving.core.ui.LivvingSuccess
import kr.osj.livving.core.ui.LivvingSummaryCard
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.addLivvingMinutes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    deadline: String,
    delayMinutes: Int,
    checked: Boolean,
    lastCheckedAt: String,
    late: Boolean,
    acceptedGuardianNames: List<String>,
    onNotificationClick: () -> Unit,
    onCheckInClick: () -> Unit,
    onRelationsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onToggleLateClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val alertAt = addLivvingMinutes(deadline, delayMinutes)
    LivvingHeader(
        title = when {
            checked -> "오늘 안부 확인 완료!"
            late -> "안부 마감 시간이 지났어요"
            else -> "오늘도 안전해요"
        },
        sub = when {
            checked -> "보호자 알림이 진행되지 않아요."
            late -> "$alertAt 전까지 확인해 주세요."
            else -> "${deadline}까지 안부를 확인해 주세요."
        },
        right = {
            LivvingIconCircle("!", onClick = onNotificationClick)
        },
    )
    if (late) {
        LivvingInfoBox(
            text = "${alertAt}까지 확인이 없으면 보호자에게 알림이 가요.",
            tone = LivvingTone.Red,
        )
        Spacer(Modifier.height(18.dp))
    }
    LivvingCheckInButton(
        checked = checked,
        onClick = onCheckInClick,
    )
    if (checked) {
        Spacer(Modifier.height(18.dp))
        LivvingCenterText(text = "마지막 확인", color = LivvingMuted)
        LivvingCenterText(text = lastCheckedAt, fontSize = 30, color = LivvingSuccess)
    }
    Spacer(Modifier.height(24.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        LivvingSummaryCard("안부 마감", deadline, "시계", LivvingCoral, Modifier.weight(1f))
        LivvingSummaryCard("보호자 알림", alertAt, "마감 ${delayMinutes}분 후", LivvingPurple, Modifier.weight(1f))
    }
    Spacer(Modifier.height(14.dp))
    LivvingCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onRelationsClick),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("내 보호자", color = LivvingMuted, fontSize = 13.sp)
                Text("${acceptedGuardianNames.size}명", fontSize = 30.sp, fontWeight = FontWeight.Black)
            }
            acceptedGuardianNames.take(2).forEachIndexed { index, name ->
                LivvingAvatar(name = name, tone = if (index == 0) LivvingTone.Coral else LivvingTone.Green)
            }
            Text(text = "›", color = LivvingMuted, fontSize = 26.sp)
        }
    }
    Spacer(Modifier.height(14.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        LivvingSecondaryButton(
            text = "안부 기록",
            modifier = Modifier.weight(1f),
            onClick = onHistoryClick,
        )
        LivvingSecondaryButton(
            text = "상태 테스트",
            modifier = Modifier.weight(1f),
            onClick = onToggleLateClick,
        )
    }
}
