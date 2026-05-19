package kr.osj.livving.feature.greeting

data class GreetingState(
    val isContentVisible: Boolean = false,
    val greeting: String = "",
)
