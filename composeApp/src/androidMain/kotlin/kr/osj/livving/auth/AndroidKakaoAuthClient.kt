package kr.osj.livving.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.osj.livving.domain.livving.KakaoLoginToken
import kr.osj.livving.domain.livving.repository.KakaoAuthClient

class AndroidKakaoAuthClient(
    private val context: Context,
) : KakaoAuthClient {
    override suspend fun login(): KakaoLoginToken = suspendCancellableCoroutine { continuation ->
        val kakaoTalkAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(context)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> continuation.resumeWithException(error)
                token?.idToken.isNullOrBlank() -> {
                    continuation.resumeWithException(
                        IllegalStateException("카카오 ID 토큰을 가져오지 못했어요."),
                    )
                }
                else -> continuation.resume(
                    KakaoLoginToken(
                        accessToken = token.accessToken,
                        idToken = token.idToken.orEmpty(),
                    ),
                )
            }
        }

        if (kakaoTalkAvailable) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error == null) {
                    callback(token, null)
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    override suspend fun logout() {
        suspendCancellableCoroutine<Unit> { continuation ->
            UserApiClient.instance.logout { error ->
                if (error == null) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(error)
                }
            }
        }
    }
}
