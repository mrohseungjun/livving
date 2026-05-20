package kr.osj.livving.feature.notifications

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingCenterText
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingPersonHeader
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingSecondaryButton
import kr.osj.livving.core.ui.LivvingTone
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RequestScreen(
    onBackClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    viewModel: RequestViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "보호자 요청",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingPersonHeader("박선영", "나를 보호자로 초대했어요", LivvingTone.Purple)
    Spacer(Modifier.height(24.dp))
    LivvingCenterText("수락하면 안부 미확인 알림을 받을 수 있어요.", color = LivvingMuted)
    Spacer(Modifier.height(36.dp))
    LivvingPrimaryButton("수락하기", onClick = onAcceptClick)
    Spacer(Modifier.height(12.dp))
    LivvingSecondaryButton("거절하기", onClick = onRejectClick)
}
