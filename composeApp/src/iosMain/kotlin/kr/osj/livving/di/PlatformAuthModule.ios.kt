package kr.osj.livving.di

import kr.osj.livving.auth.IosKakaoAuthClient
import kr.osj.livving.domain.livving.repository.KakaoAuthClient
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformAuthModule: Module = module {
    single<KakaoAuthClient> { IosKakaoAuthClient() }
}
