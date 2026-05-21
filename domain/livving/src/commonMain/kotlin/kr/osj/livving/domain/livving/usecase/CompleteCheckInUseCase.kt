package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInCompletion
import kr.osj.livving.domain.livving.repository.CheckInRepository

class CompleteCheckInUseCase(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(userId: String): CheckInCompletion {
        return repository.completeCheckIn(userId)
    }
}
