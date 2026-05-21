package kr.osj.livving.data.network.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface LivvingApi {
    @GET("rest/v1/users")
    suspend fun getUsers(
        @Query("select") select: String = "*",
        @Query("limit") limit: Int = 1,
    ): List<NetworkUser>
}

@Serializable
data class NetworkUser(
    val id: String,
    val name: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
)
