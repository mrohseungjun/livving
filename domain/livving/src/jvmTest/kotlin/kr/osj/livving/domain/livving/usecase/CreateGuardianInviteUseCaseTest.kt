package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianStatus
import kr.osj.livving.domain.livving.repository.RelationRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateGuardianInviteUseCaseTest {
    @Test
    fun createsPendingInvite() = kotlinx.coroutines.test.runTest {
        val useCase = CreateGuardianInviteUseCase(FakeRelationRepository())

        val result = useCase("user-id")

        assertEquals(GuardianStatus.Pending, result.status)
        assertEquals("invite-code", result.inviteCode)
    }

    private class FakeRelationRepository : RelationRepository {
        override suspend fun getMyGuardians(userId: String): List<Guardian> = emptyList()

        override suspend fun createGuardianInvite(userId: String): Guardian {
            return Guardian(
                id = 1,
                name = "초대받은 사람",
                relation = "미정",
                status = GuardianStatus.Pending,
                inviteCode = "invite-code",
            )
        }
    }
}
