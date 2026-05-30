package kr.osj.livving.data.supabase.di

import kr.osj.livving.data.supabase.repository.SupabaseAuthRepository
import kr.osj.livving.data.supabase.repository.SupabaseCheckInRepository
import kr.osj.livving.data.supabase.repository.SupabaseNotificationRepository
import kr.osj.livving.data.supabase.repository.SupabaseRelationRepository
import kr.osj.livving.domain.livving.repository.AuthRepository
import kr.osj.livving.domain.livving.repository.CheckInRepository
import kr.osj.livving.domain.livving.repository.NotificationRepository
import kr.osj.livving.domain.livving.repository.RelationRepository
import org.koin.dsl.module

val supabaseDataModule = module {
    single<AuthRepository> { SupabaseAuthRepository(get()) }
    single<RelationRepository> { SupabaseRelationRepository(get()) }
    single<CheckInRepository> { SupabaseCheckInRepository(get()) }
    single<NotificationRepository> { SupabaseNotificationRepository(get(), get(), get()) }
}
