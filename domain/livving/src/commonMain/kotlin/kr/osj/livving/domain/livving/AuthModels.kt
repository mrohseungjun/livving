package kr.osj.livving.domain.livving

data class LivvingUser(
    val id: String,
    val kakaoId: String,
    val nickname: String,
    val profileImageUrl: String?,
)

data class KakaoLoginToken(
    val accessToken: String,
    val idToken: String,
)

data class KakaoLoginResult(
    val user: LivvingUser,
    val isNewUser: Boolean,
)

data class AuthSession(
    val user: LivvingUser,
    val hasCompletedInitialSetup: Boolean,
    val settings: InitialUserSettings? = null,
)

data class InitialUserSettings(
    val deadline: String,
    val delayMinutes: Int,
    val pushEnabled: Boolean,
    val relationPushEnabled: Boolean,
    val missedPushEnabled: Boolean,
)
