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
