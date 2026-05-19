package kr.osj.livving.core.platform.di

import kr.osj.livving.core.platform.Platform
import kr.osj.livving.core.platform.getPlatform
import org.koin.dsl.module

val corePlatformModule = module {
    single<Platform> { getPlatform() }
}
