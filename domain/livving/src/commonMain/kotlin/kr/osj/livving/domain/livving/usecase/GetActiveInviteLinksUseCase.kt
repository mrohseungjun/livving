package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.GuardianInvite
import kr.osj.livving.domain.livving.repository.RelationRepository

class GetActiveInviteLinksUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(userId: String): List<GuardianInvite> {
        return repository.getActiveInviteLinks(userId)
    }
}
