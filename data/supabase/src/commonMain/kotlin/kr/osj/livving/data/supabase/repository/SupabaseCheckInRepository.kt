package kr.osj.livving.data.supabase.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kr.osj.livving.data.network.currentDateIso
import kr.osj.livving.data.supabase.dto.CheckInDto
import kr.osj.livving.domain.livving.CheckInCompletion
import kr.osj.livving.domain.livving.CheckInStatus
import kr.osj.livving.domain.livving.repository.CheckInRepository

class SupabaseCheckInRepository(
    private val client: SupabaseClient,
) : CheckInRepository {
    override suspend fun completeCheckIn(userId: String): CheckInCompletion {
        val checkIn = client.from("check_ins")
            .upsert(mapOf("user_id" to userId)) {
                onConflict = "user_id,check_in_date"
                select()
            }
            .decodeSingle<CheckInDto>()

        return CheckInCompletion(
            lastCheckedAt = checkIn.checkedAt,
            status = CheckInStatus.Done,
        )
    }

    override suspend fun getTodayCheckIn(userId: String): CheckInCompletion? {
        val checkIn = client.from("check_ins")
            .select {
                filter {
                    filter("user_id", FilterOperator.EQ, userId)
                    filter("check_in_date", FilterOperator.EQ, currentDateIso())
                }
            }
            .decodeList<CheckInDto>()
            .firstOrNull()

        return checkIn?.let {
            CheckInCompletion(
                lastCheckedAt = it.checkedAt,
                status = CheckInStatus.Done,
            )
        }
    }
}
