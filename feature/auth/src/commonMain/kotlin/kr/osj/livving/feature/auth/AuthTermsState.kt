package kr.osj.livving.feature.auth

data class AuthTermsState(
    val service: Boolean,
    val privacy: Boolean,
    val age: Boolean,
    val marketing: Boolean,
) {
    val required: Boolean get() = service && privacy && age
    val all: Boolean get() = service && privacy && age && marketing
}
