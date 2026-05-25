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
    userName: String,
    lastCheckedAt: String,
    message: String,
    phoneNumber: String?,
    deadline: String,
    alertAt: String,
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
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
                LivvingAvatar(userName)
                Column {
                    Text(userName, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    LivvingBadge("미확인", LivvingTone.Red)
                }
            }
            Spacer(Modifier.height(24.dp))
            LivvingInfoBox(message, tone = LivvingTone.Red)
            Spacer(Modifier.height(16.dp))
            LivvingDataRow("마지막 확인", lastCheckedAt)
            LivvingDataRow("안부 마감", deadline)
            LivvingDataRow("보호자 알림", alertAt)
        }
    }
    Spacer(Modifier.height(24.dp))
    LivvingPrimaryButton(
        text = if (phoneNumber != null) "전화하기" else "전화번호 없음",
        enabled = phoneNumber != null,
        onClick = onCallClick,
    )
    Spacer(Modifier.height(12.dp))
    LivvingSecondaryButton("확인했어요", onClick = onConfirmClick)
}
