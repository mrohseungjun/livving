package kr.osj.livving.di

import kr.osj.livving.auth.AndroidKakaoAuthClient
import kr.osj.livving.auth.AndroidKakaoAuthContext
import kr.osj.livving.domain.livving.repository.KakaoAuthClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformAuthModule: Module = module {
    single<KakaoAuthClient> {
        AndroidKakaoAuthClient(AndroidKakaoAuthContext.applicationContext)
    }
}
