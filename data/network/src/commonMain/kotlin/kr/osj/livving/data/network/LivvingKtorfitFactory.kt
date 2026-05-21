package kr.osj.livving.data.network

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient

fun createLivvingKtorfit(
    httpClient: HttpClient,
    config: NetworkConfig,
): Ktorfit = Ktorfit.Builder()
    .baseUrl(config.ktorfitBaseUrl)
    .httpClient(httpClient)
    .build()
