package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.repository.RelationRepository

class DisconnectGuardianUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(userId: String, guardianRelationId: Long) {
        repository.disconnectGuardian(userId, guardianRelationId)
    }
}
