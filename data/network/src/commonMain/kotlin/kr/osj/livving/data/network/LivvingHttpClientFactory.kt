package kr.osj.livving.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createLivvingHttpClient(
    engineFactory: HttpClientEngineFactory<*>,
    json: Json,
    config: NetworkConfig,
): HttpClient = HttpClient(engineFactory) {
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
}
