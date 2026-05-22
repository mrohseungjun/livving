package kr.osj.livving.data.network.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kr.osj.livving.data.network.dto.GuardianRelationCreateDto
import kr.osj.livving.data.network.dto.GuardianRelationDto
import kr.osj.livving.data.network.dto.InviteLinkDto
import kr.osj.livving.data.network.dto.InviteRequestDto
import kr.osj.livving.data.network.dto.ProfileDto
import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianInvite
import kr.osj.livving.domain.livving.GuardianInviteRequest
import kr.osj.livving.domain.livving.GuardianStatus
import kr.osj.livving.domain.livving.repository.RelationRepository

class SupabaseRelationRepository(
    private val client: SupabaseClient,
) : RelationRepository {
    override suspend fun getMyGuardians(userId: String): List<Guardian> {
        return client.from("guardian_relations")
            .select {
                filter {
                    filter("user_id", FilterOperator.EQ, userId)
                }
            }
            .decodeList<GuardianRelationDto>()
            .map { dto -> dto.toDomain() }
    }

    override suspend fun getActiveInviteLinks(userId: String): List<GuardianInvite> {
        return client.from("invite_links")
            .select {
                filter {
                    filter("owner_user_id", FilterOperator.EQ, userId)
                    filter("status", FilterOperator.EQ, "active")
                }
            }
            .decodeList<InviteLinkDto>()
            .map { dto -> dto.toDomain() }
    }

    override suspend fun createGuardianInvite(userId: String): GuardianInvite {
        val invite = client.from("invite_links")
            .insert(mapOf("owner_user_id" to userId)) {
                select()
            }
            .decodeSingle<InviteLinkDto>()

        return invite.toDomain()
    }

    override suspend fun getInviteRequest(inviteCode: String): GuardianInviteRequest? {
        val invite = client.from("invite_links")
            .select {
                filter {
                    filter("invite_code", FilterOperator.EQ, inviteCode)
                    filter("status", FilterOperator.EQ, "active")
                }
            }
            .decodeList<InviteRequestDto>()
            .firstOrNull()
            ?: return null

        val owner = client.from("profiles")
            .select {
                filter {
                    filter("id", FilterOperator.EQ, invite.ownerUserId)
                }
            }
            .decodeList<ProfileDto>()
            .firstOrNull()
            ?: return null

        return GuardianInviteRequest(
            inviteCode = invite.inviteCode,
            ownerUserId = invite.ownerUserId,
            ownerName = owner.nickname,
        )
    }

    override suspend fun acceptGuardianInvite(inviteCode: String, guardianUserId: String): Guardian {
        val invite = client.from("invite_links")
            .select {
                filter {
                    filter("invite_code", FilterOperator.EQ, inviteCode)
                    filter("status", FilterOperator.EQ, "active")
                }
            }
            .decodeList<InviteRequestDto>()
            .firstOrNull()
            ?: error("Active invite link is required")

        val guardian = client.from("profiles")
            .select {
                filter {
                    filter("id", FilterOperator.EQ, guardianUserId)
                }
            }
            .decodeList<ProfileDto>()
            .firstOrNull()
            ?: error("Guardian profile is required")

        val relation = client.from("guardian_relations")
            .upsert(
                GuardianRelationCreateDto(
                    userId = invite.ownerUserId,
                    guardianUserId = guardianUserId,
                    guardianName = guardian.nickname,
                    relation = "보호자",
                    status = "accepted",
                    inviteCode = invite.inviteCode,
                    inviteLinkId = invite.id,
                ),
            ) {
                onConflict = "user_id,guardian_user_id"
                select()
            }
            .decodeSingle<GuardianRelationDto>()

        return relation.toDomain()
    }

    private fun GuardianRelationDto.toDomain(): Guardian = Guardian(
        id = id,
        name = guardianName,
        relation = relation,
        status = if (status == "pending") GuardianStatus.Pending else GuardianStatus.Accepted,
        inviteCode = inviteCode,
    )

    private fun InviteLinkDto.toDomain(): GuardianInvite = GuardianInvite(
        id = id,
        inviteCode = inviteCode,
    )
}
