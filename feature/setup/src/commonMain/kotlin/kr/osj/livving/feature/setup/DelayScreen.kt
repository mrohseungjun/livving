package kr.osj.livving.feature.setup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingScheduleCard
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.LivvingTwoColumnButtons
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DelayScreen(
    deadline: String,
    delayMinutes: Int,
    fromSettings: Boolean,
    onBackClick: () -> Unit,
    onDelayClick: (Int) -> Unit,
    onSaveClick: () -> Unit,
    viewModel: DelayViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "보호자 알림 기준",
        sub = "안부 마감 시간 이후 몇 분 뒤 보호자에게 알릴지 정해요.",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingScheduleCard(deadline, delayMinutes)
    Spacer(Modifier.height(22.dp))
    LivvingTwoColumnButtons(
        items = listOf("즉시", "5분 후", "10분 후", "15분 후"),
        selected = if (delayMinutes == 0) "즉시" else "${delayMinutes}분 후",
        onClick = { label ->
            val minutes = when (label) {
                "즉시" -> 0
                "10분 후" -> 10
                "15분 후" -> 15
                else -> 5
            }
            onDelayClick(minutes)
        },
    )
    Spacer(Modifier.height(18.dp))
    LivvingInfoBox(
        text = "추천값은 5분입니다. 단순 지각은 줄이고, 보호자에게는 빠르게 알려줄 수 있어요.",
        tone = LivvingTone.Neutral,
    )
    Spacer(Modifier.height(28.dp))
    LivvingPrimaryButton(
        text = if (fromSettings) "저장하기" else "시작하기",
        onClick = onSaveClick,
    )
}
