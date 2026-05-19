package kr.osj.livving.feature.greeting

sealed interface GreetingIntent {
    data object ToggleContent : GreetingIntent
}
