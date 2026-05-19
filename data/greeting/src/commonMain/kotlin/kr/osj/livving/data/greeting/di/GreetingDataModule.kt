package kr.osj.livving.data.greeting.di

import kr.osj.livving.core.platform.Platform
import kr.osj.livving.core.platform.di.corePlatformModule
import kr.osj.livving.data.greeting.DefaultGreetingRepository
import kr.osj.livving.data.network.di.networkModule
import kr.osj.livving.domain.greeting.GreetingRepository
import org.koin.dsl.module

val greetingDataModule = module {
    includes(corePlatformModule, networkModule)
    single<GreetingRepository> {
        DefaultGreetingRepository(
            platformName = { get<Platform>().name },
        )
    }
}
