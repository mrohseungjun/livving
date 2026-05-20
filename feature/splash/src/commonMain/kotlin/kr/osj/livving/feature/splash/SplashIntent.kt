package kr.osj.livving.feature.splash

sealed interface SplashIntent {
    data object Start : SplashIntent
}
