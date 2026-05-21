package kr.osj.livving.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingCenterText
import kr.osj.livving.core.ui.LivvingCoral
import kr.osj.livving.core.ui.LivvingLogo
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingText
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.user) {
        if (state.user != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(720.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LivvingLogo()
        Spacer(Modifier.height(64.dp))
        Text(
            text = "오늘의 안부가\n내일의 안심이 됩니다",
            color = LivvingText,
            fontSize = 31.sp,
            lineHeight = 38.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(18.dp))
        LivvingCenterText(
            text = "정해진 시간까지 안부 확인이 없으면\n5분 후 보호자에게 알림을 보내요.",
            color = LivvingMuted,
        )
        Spacer(Modifier.height(56.dp))
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFF1F7)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Text("✓", color = LivvingCoral, fontSize = 56.sp, fontWeight = FontWeight.Black)
            }
        }
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(if (state.loading) Color(0xFFE5E5E5) else Color(0xFFFEE500))
                .clickable(enabled = !state.loading) {
                    viewModel.onIntent(LoginIntent.ClickKakao)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (state.loading) "로그인 중..." else "카카오로 시작하기",
                color = Color.Black,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
            )
        }
        Spacer(Modifier.height(12.dp))
        state.errorMessage?.let { message ->
            Text(
                text = message,
                color = LivvingCoral,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
        }
        LivvingCenterText(
            text = "로그인 시 약관과 개인정보처리방침에 동의하게 됩니다.",
            fontSize = 11,
            color = Color(0xFFA3A3A3),
        )
    }
}
