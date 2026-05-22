package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianInvite
import kr.osj.livving.domain.livving.GuardianInviteRequest

interface RelationRepository {
    suspend fun getMyGuardians(userId: String): List<Guardian>
    suspend fun getActiveInviteLinks(userId: String): List<GuardianInvite>
    suspend fun createGuardianInvite(userId: String): GuardianInvite
    suspend fun getInviteRequest(inviteCode: String): GuardianInviteRequest?
    suspend fun acceptGuardianInvite(inviteCode: String, guardianUserId: String): Guardian
}
