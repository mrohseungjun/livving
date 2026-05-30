package kr.osj.livving.data.network.di

import kr.osj.livving.data.network.NetworkConfig
import kr.osj.livving.data.network.api.LivvingApi
import kr.osj.livving.data.network.api.provideLivvingApi
import kr.osj.livving.data.network.createLivvingHttpClient
import kr.osj.livving.data.network.createLivvingKtorfit
import kr.osj.livving.data.network.createLivvingSupabaseClient
import kr.osj.livving.data.network.platformHttpClientEngineFactory
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        NetworkConfig(
            supabaseUrl = "https://tdeyfwpdmzklajgvwlbq.supabase.co",
            supabaseAnonKey = "sb_publishable_8f6dIWqF1W13P-jm7dS78w_zflhqVts",
        )
    }
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
    single { platformHttpClientEngineFactory() }
    single { createLivvingHttpClient(get(), get(), get()) }
    single { createLivvingKtorfit(get(), get()) }
    single { createLivvingSupabaseClient(get()) }
    single<LivvingApi> { provideLivvingApi(get()) }
}
