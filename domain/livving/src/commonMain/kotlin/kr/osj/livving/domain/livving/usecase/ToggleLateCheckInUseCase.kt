package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInStatus

class ToggleLateCheckInUseCase {
    operator fun invoke(current: CheckInStatus): CheckInStatus {
        return if (current == CheckInStatus.Late) {
            CheckInStatus.Before
        } else {
            CheckInStatus.Late
        }
    }
}
