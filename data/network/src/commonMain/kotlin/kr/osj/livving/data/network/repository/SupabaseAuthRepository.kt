package kr.osj.livving.data.network.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Kakao
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kr.osj.livving.data.network.dto.ProfileDto
import kr.osj.livving.data.network.dto.UserSettingsDto
import kr.osj.livving.domain.livving.AuthSession
import kr.osj.livving.domain.livving.InitialUserSettings
import kr.osj.livving.domain.livving.KakaoLoginResult
import kr.osj.livving.domain.livving.KakaoLoginToken
import kr.osj.livving.domain.livving.LivvingUser
import kr.osj.livving.domain.livving.repository.AuthRepository

class SupabaseAuthRepository(
    private val client: SupabaseClient,
) : AuthRepository {
    override suspend fun getCurrentSession(): AuthSession? {
        client.auth.awaitInitialization()
        val user = client.auth.currentUserOrNull() ?: return null
        val profile = client.from("profiles")
            .select {
                filter {
                    filter("id", FilterOperator.EQ, user.id)
                }
            }
            .decodeList<ProfileDto>()
            .firstOrNull()
            ?: return null
        val settings = client.from("user_settings")
            .select {
                filter {
                    filter("user_id", FilterOperator.EQ, user.id)
                }
            }
            .decodeList<UserSettingsDto>()

        return AuthSession(
            user = profile.toDomain(),
            hasCompletedInitialSetup = settings.isNotEmpty(),
            settings = settings.firstOrNull()?.toDomain(),
        )
    }

    override suspend fun loginWithKakao(token: KakaoLoginToken): KakaoLoginResult {
        client.auth.signInWith(IDToken) {
            provider = Kakao
            idToken = token.idToken
        }

        val user = client.auth.currentUserOrNull()
            ?: error("Supabase session is required after Kakao login")

        val kakaoId = user.userMetadata?.get("provider_id")?.toString()
            ?: user.userMetadata?.get("sub")?.toString()
            ?: token.accessToken
        val nickname = user.userMetadata?.get("name")?.toString()
            ?: user.userMetadata?.get("nickname")?.toString()
            ?: ""
        val profileImageUrl = user.userMetadata?.get("avatar_url")?.toString()
            ?: user.userMetadata?.get("picture")?.toString()
        val profile = client.from("profiles")
            .upsert(
                ProfileDto(
                    id = user.id,
                    kakaoId = kakaoId,
                    nickname = nickname,
                    profileImageUrl = profileImageUrl,
                ),
            ) {
                select()
            }
            .decodeSingle<ProfileDto>()

        return KakaoLoginResult(
            user = LivvingUser(
                id = profile.id,
                kakaoId = profile.kakaoId,
                nickname = profile.nickname,
                profileImageUrl = profile.profileImageUrl,
            ),
            isNewUser = false,
        )
    }

    override suspend fun saveInitialSettings(userId: String, settings: InitialUserSettings) {
        client.from("user_settings")
            .upsert(
                UserSettingsDto(
                    userId = userId,
                    deadlineTime = settings.deadline,
                    delayMinutes = settings.delayMinutes,
                    pushEnabled = settings.pushEnabled,
                    relationPushEnabled = settings.relationPushEnabled,
                    missedPushEnabled = settings.missedPushEnabled,
                ),
            )
    }

    override suspend fun logout() {
        client.auth.signOut()
    }

    private fun ProfileDto.toDomain(): LivvingUser = LivvingUser(
        id = id,
        kakaoId = kakaoId,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
    )

    private fun UserSettingsDto.toDomain(): InitialUserSettings = InitialUserSettings(
        deadline = deadlineTime.take(5),
        delayMinutes = delayMinutes,
        pushEnabled = pushEnabled,
        relationPushEnabled = relationPushEnabled,
        missedPushEnabled = missedPushEnabled,
    )
}
