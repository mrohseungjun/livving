package kr.osj.livving.data.network.di

import kr.osj.livving.data.network.NetworkConfig
import kr.osj.livving.data.network.api.LivvingApi
import kr.osj.livving.data.network.api.provideLivvingApi
import kr.osj.livving.data.network.createLivvingHttpClient
import kr.osj.livving.data.network.createLivvingKtorfit
import kr.osj.livving.data.network.createLivvingSupabaseClient
import kr.osj.livving.data.network.platformHttpClientEngineFactory
import kr.osj.livving.data.network.repository.SupabaseAuthRepository
import kr.osj.livving.data.network.repository.SupabaseCheckInRepository
import kr.osj.livving.data.network.repository.SupabaseRelationRepository
import kr.osj.livving.domain.livving.repository.AuthRepository
import kr.osj.livving.domain.livving.repository.CheckInRepository
import kr.osj.livving.domain.livving.repository.RelationRepository
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
    single { createLivvingKtorfit(get(), get()) }
    single { createLivvingSupabaseClient(get()) }
    single<AuthRepository> { SupabaseAuthRepository(get()) }
    single<RelationRepository> { SupabaseRelationRepository(get()) }
    single<CheckInRepository> { SupabaseCheckInRepository(get()) }
    single<LivvingApi> { provideLivvingApi(get()) }
}
