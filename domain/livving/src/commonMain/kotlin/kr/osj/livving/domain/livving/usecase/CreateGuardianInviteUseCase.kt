package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.GuardianStatus

class CreateGuardianInviteUseCase {
    operator fun invoke(current: List<Guardian>): List<Guardian> {
        if (current.any { guardian -> guardian.id == PendingInviteId }) return current

        return current + Guardian(
            id = PendingInviteId,
            name = "초대받은 사람",
            relation = "미정",
            status = GuardianStatus.Pending,
        )
    }

    private companion object {
        const val PendingInviteId = 4L
    }
}
