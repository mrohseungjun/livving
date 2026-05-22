package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.Guardian
import kr.osj.livving.domain.livving.repository.RelationRepository

class GetMyGuardiansUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(userId: String): List<Guardian> {
        return repository.getMyGuardians(userId)
    }
}
