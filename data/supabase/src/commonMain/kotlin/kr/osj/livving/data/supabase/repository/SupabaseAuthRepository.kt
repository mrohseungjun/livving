package kr.osj.livving.data.supabase.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Kakao
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kr.osj.livving.data.supabase.dto.ProfileDto
import kr.osj.livving.data.supabase.dto.UserContactSettingDto
import kr.osj.livving.data.supabase.dto.UserSettingsDto
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
        val contact = getContactSetting(user.id)

        return AuthSession(
            user = profile.toDomain(contact),
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

    override suspend fun savePhoneContact(userId: String, phoneNumber: String?, phoneCallEnabled: Boolean): LivvingUser {
        val normalizedPhoneNumber = phoneNumber?.trim().orEmpty().ifBlank { null }
        val contact = client.from("user_contact_settings")
            .upsert(
                UserContactSettingDto(
                    userId = userId,
                    phoneNumber = normalizedPhoneNumber,
                    phoneCallEnabled = phoneCallEnabled && normalizedPhoneNumber != null,
                ),
            ) {
                select()
            }
            .decodeSingle<UserContactSettingDto>()
        val profile = client.from("profiles")
            .select {
                filter {
                    filter("id", FilterOperator.EQ, userId)
                }
            }
            .decodeSingle<ProfileDto>()
        return profile.toDomain(contact)
    }

    override suspend fun logout() {
        client.auth.signOut()
    }

    private suspend fun getContactSetting(userId: String): UserContactSettingDto? {
        return client.from("user_contact_settings")
            .select {
                filter {
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }
            .decodeList<UserContactSettingDto>()
            .firstOrNull()
    }

    private fun ProfileDto.toDomain(contact: UserContactSettingDto? = null): LivvingUser = LivvingUser(
        id = id,
        kakaoId = kakaoId,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        phoneNumber = contact?.phoneNumber,
        phoneCallEnabled = contact?.phoneCallEnabled == true,
    )

    private fun UserSettingsDto.toDomain(): InitialUserSettings = InitialUserSettings(
        deadline = deadlineTime.take(5),
        delayMinutes = delayMinutes,
        pushEnabled = pushEnabled,
        relationPushEnabled = relationPushEnabled,
        missedPushEnabled = missedPushEnabled,
    )
}
