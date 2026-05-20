package kr.osj.livving

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.osj.livving.core.ui.LivvingBackground
import kr.osj.livving.core.ui.LivvingLogo
import kr.osj.livving.core.ui.LivvingMuted
import kr.osj.livving.core.ui.LivvingTheme

class SplashActivity : ComponentActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val openMain = Runnable {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            LivvingTheme {
                SplashScreen()
            }
        }

        handler.postDelayed(openMain, SPLASH_DURATION_MILLIS)
    }

    override fun onDestroy() {
        handler.removeCallbacks(openMain)
        super.onDestroy()
    }

    companion object {
        private const val SPLASH_DURATION_MILLIS = 1_200L
    }
}

@Composable
private fun SplashScreen() {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.94f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(durationMillis = 420))
        scale.animateTo(1f, tween(durationMillis = 360))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LivvingBackground),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
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
