package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.CheckInRequestResult
import kr.osj.livving.domain.livving.LivvingNotification
import kr.osj.livving.domain.livving.PushTokenRegistration
import kr.osj.livving.domain.livving.TestNotificationResult
import kr.osj.livving.domain.livving.repository.NotificationRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SendCheckInRequestUseCaseTest {
    @Test
    fun trimsMessageAndSendsRequest() = runTest {
        val repository = FakeNotificationRepository()
        val result = SendCheckInRequestUseCase(repository)(
            guardianUserId = "guardian-id",
            targetUserId = "target-id",
            message = "  안부 확인 부탁해요  ",
        )

        assertEquals(1, result.sentCount)
        assertEquals("guardian-id", repository.guardianUserId)
        assertEquals("target-id", repository.targetUserId)
        assertEquals("안부 확인 부탁해요", repository.message)
    }

    private class FakeNotificationRepository : NotificationRepository {
        var guardianUserId = ""
        var targetUserId = ""
        var message = ""

        override suspend fun registerPushToken(userId: String, registration: PushTokenRegistration) = Unit
        override suspend fun disablePushToken(userId: String, token: String) = Unit
        override suspend fun getNotifications(userId: String): List<LivvingNotification> = emptyList()
        override suspend fun markNotificationRead(userId: String, notificationId: String) = Unit
        override suspend fun sendTestNotification(userId: String): TestNotificationResult = TestNotificationResult(0, 0, 0)

        override suspend fun sendCheckInRequest(
            guardianUserId: String,
            targetUserId: String,
            message: String,
        ): CheckInRequestResult {
            this.guardianUserId = guardianUserId
            this.targetUserId = targetUserId
            this.message = message
            return CheckInRequestResult(sentCount = 1, failedCount = 0)
        }
    }
}
