package kr.osj.livving.feature.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingScheduleCard
import kr.osj.livving.core.ui.LivvingTone
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScheduleScreen(
    deadline: String,
    delayMinutes: Int,
    onBackClick: () -> Unit,
    viewModel: ScheduleViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "알림 흐름",
        sub = "보호자도 이 시간을 볼 수 있어요.",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingScheduleCard(deadline, delayMinutes)
    Spacer(Modifier.height(18.dp))
    LivvingInfoBox("시간은 본인이 수정할 수 있고, 변경 내용은 보호자에게 알림으로 공유됩니다.", tone = LivvingTone.Orange)
}
