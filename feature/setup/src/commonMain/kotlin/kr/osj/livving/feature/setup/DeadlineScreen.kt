package kr.osj.livving.feature.setup

import androidx.compose.foundation.layout.Column
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
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.LivvingTwoColumnButtons
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeadlineScreen(
    selectedDeadline: String,
    fromSettings: Boolean,
    onBackClick: () -> Unit,
    onDeadlineClick: (String) -> Unit,
    onSaveClick: () -> Unit,
    viewModel: DeadlineViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = if (fromSettings) "안부 마감 시간 변경" else "안부 확인 마감 시간을\n설정해 주세요",
        sub = if (fromSettings) {
            "보호자가 여러 명이어도 시간은 본인이 직접 바꿀 수 있어요. 대신 변경 내용은 보호자에게 공유됩니다."
        } else {
            "이 시간까지 확인이 없으면 보호자 알림 기준이 시작돼요."
        },
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(selectedDeadline, fontSize = 56.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(14.dp))
            Text("변경 시 다음 안부 확인일부터 적용됩니다.", color = LivvingMuted)
        }
    }
    Spacer(Modifier.height(18.dp))
    LivvingTwoColumnButtons(
        items = listOf("07:30", "08:30", "09:00", "10:00"),
        selected = selectedDeadline,
        onClick = onDeadlineClick,
    )
    Spacer(Modifier.height(18.dp))
    LivvingInfoBox(
        text = "보호자는 내 안부 마감 시간과 변경 이력을 볼 수 있습니다.",
        tone = LivvingTone.Coral,
    )
    Spacer(Modifier.height(28.dp))
    LivvingPrimaryButton(
        text = if (fromSettings) "변경하고 보호자에게 알리기" else "다음",
        onClick = onSaveClick,
    )
}
