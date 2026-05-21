package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.Guardian

interface RelationRepository {
    suspend fun getMyGuardians(userId: String): List<Guardian>
    suspend fun createGuardianInvite(userId: String): Guardian
}
