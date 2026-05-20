package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class CompleteCheckInUseCaseTest {
    @Test
    fun returnsDoneCheckInCompletion() {
        val result = CompleteCheckInUseCase()()

        assertEquals(CheckInStatus.Done, result.status)
        assertEquals("오늘 08:25", result.lastCheckedAt)
    }
}
