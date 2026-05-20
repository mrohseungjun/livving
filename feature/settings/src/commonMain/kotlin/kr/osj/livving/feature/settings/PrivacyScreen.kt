package kr.osj.livving.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingPolicyItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrivacyScreen(
    onBackClick: () -> Unit,
    viewModel: PrivacyViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "개인정보 처리",
        sub = "필요한 최소 정보만 사용합니다.",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingCard {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            LivvingPolicyItem("카카오 식별값", "로그인 및 회원 구분")
            LivvingPolicyItem("푸시 토큰", "보호자 요청/미확인 알림 발송")
            LivvingPolicyItem("안부 확인 기록", "오늘 확인 여부 판단")
            LivvingPolicyItem("보호 관계 정보", "누가 누구를 보호하는지 관리")
        }
    }
}
