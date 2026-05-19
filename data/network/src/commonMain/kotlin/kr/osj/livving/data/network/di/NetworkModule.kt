package kr.osj.livving.data.network.di

import kr.osj.livving.data.network.NetworkConfig
import kr.osj.livving.data.network.createLivvingHttpClient
import kr.osj.livving.data.network.platformHttpClientEngineFactory
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single { NetworkConfig() }
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
    single { platformHttpClientEngineFactory() }
    single { createLivvingHttpClient(get(), get(), get()) }
}
