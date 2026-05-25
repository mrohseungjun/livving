package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianInvite
import kr.osj.livving.domain.livving.GuardianInviteRequest
import kr.osj.livving.domain.livving.WatchingUser

interface RelationRepository {
    suspend fun getMyGuardians(userId: String): List<Guardian>
    suspend fun getWatchingUsers(guardianUserId: String): List<WatchingUser>
    suspend fun getActiveInviteLinks(userId: String): List<GuardianInvite>
    suspend fun createGuardianInvite(userId: String): GuardianInvite
    suspend fun getInviteRequest(inviteCode: String): GuardianInviteRequest?
    suspend fun getInviteRequestByOwner(ownerUserId: String): GuardianInviteRequest?
    suspend fun acceptGuardianInvite(inviteCode: String, guardianUserId: String): Guardian
    suspend fun disconnectGuardian(userId: String, guardianRelationId: Long)
}
