package kr.osj.livving.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingCheckDot
import kr.osj.livving.core.ui.LivvingCoralSoft
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingPrimaryButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TermsScreen(
    state: AuthTermsState,
    onBackClick: () -> Unit,
    onToggleAllClick: () -> Unit,
    onToggleServiceClick: () -> Unit,
    onTogglePrivacyClick: () -> Unit,
    onToggleAgeClick: () -> Unit,
    onToggleMarketingClick: () -> Unit,
    onContinueClick: () -> Unit,
    viewModel: TermsViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "약관 동의",
        sub = "서비스 이용을 위해 필요한 항목이에요.",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingCard {
        Column(modifier = Modifier.height(344.dp)) {
            TermRow("전체 동의", state.all, true, onToggleAllClick)
            TermRow("[필수] 서비스 이용약관", state.service, onClick = onToggleServiceClick)
            TermRow("[필수] 개인정보 수집 및 이용 동의", state.privacy, onClick = onTogglePrivacyClick)
            TermRow("[필수] 만 14세 이상입니다", state.age, onClick = onToggleAgeClick)
            TermRow("[선택] 마케팅 정보 수신 동의", state.marketing, onClick = onToggleMarketingClick)
        }
    }
    Spacer(Modifier.height(32.dp))
    LivvingPrimaryButton(
        text = "동의하고 계속하기",
        enabled = state.required,
        onClick = onContinueClick,
    )
}

@Composable
private fun TermRow(
    text: String,
    checked: Boolean,
    highlighted: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(if (highlighted) Modifier.background(LivvingCoralSoft) else Modifier)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.size(12.dp))
        LivvingCheckDot(checked = checked, onClick = onClick)
        Spacer(Modifier.size(12.dp))
        Text(modifier = Modifier.weight(1f), text = text, fontWeight = FontWeight.Bold)
        Text(text = "›", color = LivvingMuted, fontSize = 20.sp)
        Spacer(Modifier.size(12.dp))
    }
}
