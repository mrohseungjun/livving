package kr.osj.livving.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingLogo
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    canFinish: Boolean,
    onFinished: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.94f) }

    LaunchedEffect(Unit) {
        viewModel.onIntent(SplashIntent.Start)
        alpha.animateTo(1f, tween(durationMillis = 420))
        scale.animateTo(1f, tween(durationMillis = 360))
    }

    LaunchedEffect(state.isFinished, canFinish) {
        if (state.isFinished && canFinish) {
            onFinished()
        }
    }

    LivvingScreen {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(alpha.value)
                .scale(scale.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LivvingLogo()
            Spacer(Modifier.height(18.dp))
            Text(
                text = "오늘의 안부가 내일의 안심이 됩니다",
                color = LivvingMuted,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
