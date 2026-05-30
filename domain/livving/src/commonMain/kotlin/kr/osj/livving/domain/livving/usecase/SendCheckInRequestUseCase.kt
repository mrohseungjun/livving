package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInRequestResult
import kr.osj.livving.domain.livving.repository.NotificationRepository

class SendCheckInRequestUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(guardianUserId: String, targetUserId: String, message: String): CheckInRequestResult {
        require(guardianUserId.isNotBlank()) { "Guardian user id is required" }
        require(targetUserId.isNotBlank()) { "Target user id is required" }
        require(message.isNotBlank()) { "Message is required" }
        return repository.sendCheckInRequest(
            guardianUserId = guardianUserId,
            targetUserId = targetUserId,
            message = message.trim(),
        )
    }
}
