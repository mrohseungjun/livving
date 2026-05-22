package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.repository.NotificationRepository

class MarkNotificationReadUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(userId: String, notificationId: String) {
        repository.markNotificationRead(userId, notificationId)
    }
}
