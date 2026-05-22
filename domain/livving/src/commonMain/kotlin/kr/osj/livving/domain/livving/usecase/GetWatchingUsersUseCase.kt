package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.WatchingUser
import kr.osj.livving.domain.livving.repository.RelationRepository

class GetWatchingUsersUseCase(
    private val repository: RelationRepository,
) {
    suspend operator fun invoke(userId: String): List<WatchingUser> {
        return repository.getWatchingUsers(userId)
    }
}
