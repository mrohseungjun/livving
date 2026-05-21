package kr.osj.livving.data.network.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kr.osj.livving.data.network.dto.CheckInDto
import kr.osj.livving.domain.livving.CheckInCompletion
import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.repository.CheckInRepository

class SupabaseCheckInRepository(
    private val client: SupabaseClient,
) : CheckInRepository {
    override suspend fun completeCheckIn(userId: String): CheckInCompletion {
        val checkIn = client.from("check_ins")
            .insert(mapOf("user_id" to userId)) {
                select()
            }
            .decodeSingle<CheckInDto>()

        return CheckInCompletion(
            lastCheckedAt = checkIn.checkedAt,
            status = CheckInStatus.Done,
        )
    }
}
