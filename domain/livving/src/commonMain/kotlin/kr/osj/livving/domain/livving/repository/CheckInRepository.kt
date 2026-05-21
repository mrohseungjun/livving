package kr.osj.livving.domain.livving.repository

import kr.osj.livving.domain.livving.CheckInCompletion

interface CheckInRepository {
    suspend fun completeCheckIn(userId: String): CheckInCompletion
    suspend fun getTodayCheckIn(userId: String): CheckInCompletion?
}
