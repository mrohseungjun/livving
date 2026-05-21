package kr.osj.livving.data.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

fun createLivvingSupabaseClient(
    config: NetworkConfig,
): SupabaseClient = createSupabaseClient(
    supabaseUrl = config.supabaseClientUrl,
    supabaseKey = config.supabaseAnonKey,
) {
    install(Auth)
    install(Postgrest)
    install(Storage)
}
