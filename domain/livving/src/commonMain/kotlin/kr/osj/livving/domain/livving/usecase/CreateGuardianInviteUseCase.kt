package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.repository.RelationRepository

class CreateGuardianInviteUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(userId: String): Guardian {
        return repository.createGuardianInvite(userId)
    }
}
