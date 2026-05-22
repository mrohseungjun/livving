package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.GuardianInviteRequest
import kr.osj.livving.domain.livving.repository.RelationRepository

class GetGuardianInviteRequestUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(inviteCode: String): GuardianInviteRequest? {
        return repository.getInviteRequest(inviteCode)
    }
}
