package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.repository.RelationRepository

class AcceptGuardianInviteUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(inviteCode: String, guardianUserId: String): Guardian {
        return repository.acceptGuardianInvite(inviteCode, guardianUserId)
    }
}
