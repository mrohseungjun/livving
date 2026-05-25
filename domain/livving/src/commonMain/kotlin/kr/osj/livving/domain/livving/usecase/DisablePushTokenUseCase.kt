package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.repository.NotificationRepository

class DisablePushTokenUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(userId: String, token: String) {
        repository.disablePushToken(userId, token)
    }
}
