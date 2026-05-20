package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateGuardianInviteUseCaseTest {
    @Test
    fun appendsPendingInviteOnlyOnce() {
        val useCase = CreateGuardianInviteUseCase()
        val guardians = listOf(Guardian(1, "김지연", "딸", GuardianStatus.Accepted))

        val first = useCase(guardians)
        val second = useCase(first)

        assertEquals(2, first.size)
        assertEquals(2, second.size)
        assertEquals(GuardianStatus.Pending, first.last().status)
    }
}
