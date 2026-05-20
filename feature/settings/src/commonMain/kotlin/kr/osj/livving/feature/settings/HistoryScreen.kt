package kr.osj.livving.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingGradientCard
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingMuted
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "안부 기록",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingGradientCard(Modifier.fillMaxWidth()) {
        Column {
            Text("이번 주 안부 확인률", color = LivvingMuted, fontSize = 13.sp)
            Text("86%", fontWeight = FontWeight.Black, fontSize = 34.sp)
        }
    }
    Spacer(Modifier.height(20.dp))
    listOf("오늘 08:25 확인 완료", "어제 08:10 확인 완료", "5월 17일 08:35 보호자 알림", "5월 16일 08:40 확인 완료").forEach {
        LivvingCard(Modifier.fillMaxWidth()) {
            Text(modifier = Modifier.padding(16.dp), text = it, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
    }
}
