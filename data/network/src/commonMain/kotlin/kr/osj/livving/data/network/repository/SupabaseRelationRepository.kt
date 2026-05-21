package kr.osj.livving.data.network.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kr.osj.livving.data.network.dto.GuardianRelationDto
import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianStatus
import kr.osj.livving.domain.livving.repository.RelationRepository

class SupabaseRelationRepository(
    private val client: SupabaseClient,
) : RelationRepository {
    override suspend fun getMyGuardians(userId: String): List<Guardian> {
        return client.from("guardian_relations")
            .select()
            .decodeList<GuardianRelationDto>()
            .map { dto -> dto.toDomain() }
    }

    override suspend fun createGuardianInvite(userId: String): Guardian {
        error("Guardian invite creation requires invite-code generation policy")
    }

    private fun GuardianRelationDto.toDomain(): Guardian = Guardian(
        id = id,
        name = guardianName,
        relation = relation,
        status = if (status == "pending") GuardianStatus.Pending else GuardianStatus.Accepted,
    )
}
