package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.CheckInCompletion
import kr.osj.livving.domain.livving.repository.CheckInRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CompleteCheckInUseCaseTest {
    @Test
    fun returnsDoneCheckInCompletion() = runTest {
        val result = CompleteCheckInUseCase(FakeCheckInRepository())("user-id")

        assertEquals(CheckInStatus.Done, result.status)
        assertEquals("오늘 08:25", result.lastCheckedAt)
    }

    private class FakeCheckInRepository : CheckInRepository {
        override suspend fun completeCheckIn(userId: String): CheckInCompletion {
            return CheckInCompletion(
                lastCheckedAt = "오늘 08:25",
                status = CheckInStatus.Done,
            )
        }

        override suspend fun getTodayCheckIn(userId: String): CheckInCompletion? = null
    }
}
