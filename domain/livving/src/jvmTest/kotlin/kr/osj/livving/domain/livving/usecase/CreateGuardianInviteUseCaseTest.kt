package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianInvite
import kr.osj.livving.domain.livving.GuardianInviteRequest
import kr.osj.livving.domain.livving.GuardianStatus
import kr.osj.livving.domain.livving.WatchingUser
import kr.osj.livving.domain.livving.repository.RelationRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateGuardianInviteUseCaseTest {
    @Test
    fun createsInviteLink() = runTest {
        val useCase = CreateGuardianInviteUseCase(FakeRelationRepository())

        val result = useCase("user-id")

        assertEquals("invite-id", result.id)
        assertEquals("invite-code", result.inviteCode)
    }

    private class FakeRelationRepository : RelationRepository {
        override suspend fun getMyGuardians(userId: String): List<Guardian> = emptyList()
        override suspend fun getWatchingUsers(guardianUserId: String): List<WatchingUser> = emptyList()
        override suspend fun getActiveInviteLinks(userId: String): List<GuardianInvite> = emptyList()

        override suspend fun createGuardianInvite(userId: String): GuardianInvite {
            return GuardianInvite(
                id = "invite-id",
                inviteCode = "invite-code",
            )
        }

        override suspend fun getInviteRequest(inviteCode: String): GuardianInviteRequest? = null

        override suspend fun acceptGuardianInvite(inviteCode: String, guardianUserId: String): Guardian {
            return Guardian(
                id = 1,
                name = "보호자",
                relation = "보호자",
                status = GuardianStatus.Accepted,
            )
        }

        override suspend fun disconnectGuardian(userId: String, guardianRelationId: Long) = Unit
    }
}
