package kr.osj.livving.feature.settings

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
import kr.osj.livving.core.ui.LivvingTone
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "내 정보",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingPersonHeader("오승준", "카카오 로그인 계정", LivvingTone.Purple)
    Spacer(Modifier.height(28.dp))
    LivvingCard {
        Column(Modifier.padding(18.dp)) {
            LivvingDataRow("로그인 방식", "카카오")
            LivvingDataRow("내 보호자", "2명")
            LivvingDataRow("내가 보호 중", "3명")
        }
    }
}
