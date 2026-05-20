package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ToggleLateCheckInUseCaseTest {
    @Test
    fun togglesBetweenBeforeAndLate() {
        val useCase = ToggleLateCheckInUseCase()

        assertEquals(CheckInStatus.Late, useCase(CheckInStatus.Before))
        assertEquals(CheckInStatus.Before, useCase(CheckInStatus.Late))
    }
}
