package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.TestNotificationResult
import kr.osj.livving.domain.livving.repository.NotificationRepository

class SendTestNotificationUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(userId: String): TestNotificationResult {
        return repository.sendTestNotification(userId)
    }
}
