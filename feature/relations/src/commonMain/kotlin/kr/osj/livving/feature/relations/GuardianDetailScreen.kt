package kr.osj.livving.feature.relations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingDataRow
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingPersonHeader
import kr.osj.livving.core.ui.LivvingSecondaryButton
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.addLivvingMinutes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GuardianDetailScreen(
    name: String,
    relation: String,
    connected: Boolean,
    phoneNumber: String?,
    deadline: String,
    delayMinutes: Int,
    onBackClick: () -> Unit,
    onCallClick: () -> Unit,
    onDisconnectClick: () -> Unit,
    viewModel: GuardianDetailViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "보호자 상세",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingPersonHeader(name, "$relation · ${if (connected) "연결됨" else "초대 대기"}", LivvingTone.Coral)
    Spacer(Modifier.height(28.dp))
    LivvingCard {
        Column(Modifier.padding(18.dp)) {
            LivvingDataRow("상태", if (connected) "연결됨" else "초대 대기")
            LivvingDataRow("전화 연결", if (phoneNumber != null) "허용됨" else "미제공")
            LivvingDataRow("안부 마감 시간", deadline)
            LivvingDataRow("보호자 알림", "${addLivvingMinutes(deadline, delayMinutes)} (${delayMinutes}분 후)")
            LivvingDataRow("받는 알림", "미확인 / SOS")
        }
    }
    Spacer(Modifier.height(24.dp))
    LivvingSecondaryButton(
        text = if (phoneNumber != null) "전화하기" else "전화번호 없음",
        enabled = phoneNumber != null,
        onClick = onCallClick,
    )
    Spacer(Modifier.height(12.dp))
    LivvingSecondaryButton("연결 해제", danger = true, onClick = onDisconnectClick)
}
