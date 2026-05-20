package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInCompletion
import kr.osj.livving.domain.livving.CheckInStatus

class CompleteCheckInUseCase {
    operator fun invoke(): CheckInCompletion = CheckInCompletion(
        lastCheckedAt = "오늘 08:25",
        status = CheckInStatus.Done,
    )
}
