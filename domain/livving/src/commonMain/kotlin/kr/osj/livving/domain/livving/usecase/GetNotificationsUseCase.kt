package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.LivvingNotification
import kr.osj.livving.domain.livving.repository.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(userId: String): List<LivvingNotification> {
        return repository.getNotifications(userId)
    }
}
