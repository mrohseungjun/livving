package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.PushTokenRegistration
import kr.osj.livving.domain.livving.repository.NotificationRepository

class RegisterPushTokenUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(userId: String, registration: PushTokenRegistration) {
        repository.registerPushToken(userId, registration)
    }
}
