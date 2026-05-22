package kr.osj.livving.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingCard
import kr.osj.livving.core.ui.LivvingDataRow
import kr.osj.livving.core.ui.LivvingHeader
import kr.osj.livving.core.ui.LivvingPersonHeader
import kr.osj.livving.core.ui.LivvingPrimaryButton
import kr.osj.livving.core.ui.LivvingTextField
import kr.osj.livving.core.ui.LivvingTone
import kr.osj.livving.core.ui.LivvingToggleRow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    userName: String,
    guardianCount: Int,
    watchingCount: Int,
    phoneNumber: String,
    phoneCallEnabled: Boolean,
    onBackClick: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onPhoneCallEnabledToggle: () -> Unit,
    onPhoneSaveClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    LivvingHeader(
        title = "내 정보",
        showLogo = false,
        onBack = onBackClick,
    )
    LivvingPersonHeader(userName, "카카오 로그인 계정", LivvingTone.Purple)
    Spacer(Modifier.height(28.dp))
    LivvingCard {
        Column(Modifier.padding(18.dp)) {
            LivvingDataRow("로그인 방식", "카카오")
            LivvingDataRow("내 보호자", "${guardianCount}명")
            LivvingDataRow("내가 보호 중", "${watchingCount}명")
        }
    }
    Spacer(Modifier.height(18.dp))
    LivvingCard {
        Column(Modifier.padding(18.dp)) {
            Text("전화 연결", fontSize = 17.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))
            LivvingTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = "전화번호 입력",
            )
            Spacer(Modifier.height(12.dp))
            LivvingToggleRow("관계 사용자에게 전화 허용", phoneCallEnabled, onPhoneCallEnabledToggle)
            Spacer(Modifier.height(12.dp))
            LivvingPrimaryButton(
                text = "전화번호 저장",
                enabled = phoneNumber.isNotBlank() || !phoneCallEnabled,
                onClick = onPhoneSaveClick,
            )
        }
    }
}
