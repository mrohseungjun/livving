package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.GuardianInviteRequest
import kr.osj.livving.domain.livving.repository.RelationRepository

class GetGuardianInviteRequestByOwnerUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(ownerUserId: String): GuardianInviteRequest? {
        return repository.getInviteRequestByOwner(ownerUserId)
    }
}
