package kr.osj.livving.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LivvingCoral = Color(0xFFFF655A)
val LivvingPurple = Color(0xFF7E4CFF)
val LivvingBackground = Color(0xFFFFFDFC)
val LivvingSurface = Color.White
val LivvingText = Color(0xFF171717)
val LivvingMuted = Color(0xFF737373)
val LivvingLine = Color(0xFFF5F5F5)
val LivvingSuccess = Color(0xFF16A34A)
val LivvingWarning = Color(0xFFF97316)
val LivvingDanger = Color(0xFFEF4444)

private val LivvingColorScheme = lightColorScheme(
    primary = LivvingCoral,
    secondary = LivvingPurple,
    background = LivvingBackground,
    surface = LivvingSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LivvingText,
    onSurface = LivvingText,
)

@Composable
fun LivvingTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LivvingColorScheme,
        content = content,
    )
}
