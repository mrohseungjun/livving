package kr.osj.livving.feature.relations

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingFlowCard
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingPersonHeader
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingSecondaryButton
import kr.osj.livving.core.ui.LivvingTone
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InviteStatusScreen(
    onBackClick: () -> Unit,
    viewModel: InviteStatusViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "초대 상태",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingPersonHeader("박선영", "초대 대기", LivvingTone.Orange)
    Spacer(Modifier.height(28.dp))
    LivvingFlowCard(
        firstTitle = "그룹 초대 링크 생성 완료",
        firstDesc = "링크를 누른 사람마다 pending 상태 저장",
        secondTitle = "보호자별 수락 대기",
        secondDesc = "각자 수락하면 accepted로 변경",
    )
    Spacer(Modifier.height(24.dp))
    LivvingPrimaryButton("초대 다시 공유") {}
    Spacer(Modifier.height(12.dp))
    LivvingSecondaryButton("링크 복사") {}
}
