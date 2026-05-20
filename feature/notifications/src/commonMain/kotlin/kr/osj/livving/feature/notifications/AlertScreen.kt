package kr.osj.livving.feature.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingAvatar
import kr.osj.livving.core.ui.LivvingBadge
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingDataRow
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingInfoBox
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingSecondaryButton
import kr.osj.livving.core.ui.LivvingTone
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AlertScreen(
    deadline: String,
    alertAt: String,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    viewModel: AlertViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "안부 미확인",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingCard {
        Column(Modifier.padding(20.dp)) {
            Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)) {
                LivvingAvatar("오승준")
                Column {
                    Text("오승준", fontSize = 24.sp, fontWeight = FontWeight.Black)
                    LivvingBadge("미확인", LivvingTone.Red)
                }
            }
            Spacer(Modifier.height(24.dp))
            LivvingInfoBox("설정된 시간까지 안부 확인이 없어요. 가볍게 연락해 확인해 주세요.", tone = LivvingTone.Red)
            Spacer(Modifier.height(16.dp))
            LivvingDataRow("마지막 확인", "어제 08:10")
            LivvingDataRow("안부 마감", deadline)
            LivvingDataRow("보호자 알림", alertAt)
        }
    }
    Spacer(Modifier.height(24.dp))
    LivvingPrimaryButton("전화하기") {}
    Spacer(Modifier.height(12.dp))
    LivvingSecondaryButton("확인했어요", onClick = onConfirmClick)
}
