package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.LivvingNotification
import kr.osj.livving.domain.livving.PushTokenRegistration
import kr.osj.livving.domain.livving.CheckInRequestResult
import kr.osj.livving.domain.livving.TestNotificationResult

interface NotificationRepository {
    suspend fun registerPushToken(userId: String, registration: PushTokenRegistration)
    suspend fun disablePushToken(userId: String, token: String)
    suspend fun getNotifications(userId: String): List<LivvingNotification>
    suspend fun markNotificationRead(userId: String, notificationId: String)
    suspend fun sendTestNotification(userId: String): TestNotificationResult
    suspend fun sendCheckInRequest(guardianUserId: String, targetUserId: String, message: String): CheckInRequestResult
}
