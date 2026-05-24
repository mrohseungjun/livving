package kr.osj.livving.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

fun createLivvingHttpClient(
    engineFactory: HttpClientEngineFactory<*>,
    json: Json,
    config: NetworkConfig,
): HttpClient = HttpClient(engineFactory) {
    expectSuccess = true

    install(ContentNegotiation) {
        json(json)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 15_000
    }

    defaultRequest {
        if (config.supabaseUrl.isNotBlank()) {
            url(config.supabaseUrl)
        }
        contentType(ContentType.Application.Json)
        if (config.supabaseAnonKey.isNotBlank()) {
            header("apikey", config.supabaseAnonKey)
            header("Authorization", "Bearer ${config.supabaseAnonKey}")
        }
    }

    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            throw cause.toLivvingNetworkException()
        }
    }
}

sealed class LivvingNetworkException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause) {
    data class ClientError(
        val statusCode: Int,
        override val cause: Throwable,
    ) : LivvingNetworkException("요청을 처리할 수 없습니다. 잠시 후 다시 시도해 주세요.", cause)

    data class Unauthorized(
        override val cause: Throwable,
    ) : LivvingNetworkException("로그인이 만료됐어요. 다시 로그인해 주세요.", cause)

    data class ServerError(
        val statusCode: Int,
        override val cause: Throwable,
    ) : LivvingNetworkException("서버 연결이 불안정해요. 잠시 후 다시 시도해 주세요.", cause)

    data class Timeout(
        override val cause: Throwable,
    ) : LivvingNetworkException("네트워크 응답이 지연되고 있어요. 연결 상태를 확인해 주세요.", cause)

    data class Offline(
        override val cause: Throwable,
    ) : LivvingNetworkException("인터넷 연결을 확인해 주세요.", cause)

    data class Unknown(
        override val cause: Throwable,
    ) : LivvingNetworkException("알 수 없는 네트워크 오류가 발생했어요.", cause)
}

private fun Throwable.toLivvingNetworkException(): LivvingNetworkException {
    return when (this) {
        is LivvingNetworkException -> this
        is HttpRequestTimeoutException -> LivvingNetworkException.Timeout(this)
        is ClientRequestException -> {
            if (response.status.value == 401) {
                LivvingNetworkException.Unauthorized(this)
            } else {
                LivvingNetworkException.ClientError(response.status.value, this)
            }
        }
        is RedirectResponseException -> LivvingNetworkException.ClientError(response.status.value, this)
        is ServerResponseException -> LivvingNetworkException.ServerError(response.status.value, this)
        is ResponseException -> LivvingNetworkException.Unknown(this)
        is IOException -> LivvingNetworkException.Offline(this)
        else -> LivvingNetworkException.Unknown(this)
    }
}
