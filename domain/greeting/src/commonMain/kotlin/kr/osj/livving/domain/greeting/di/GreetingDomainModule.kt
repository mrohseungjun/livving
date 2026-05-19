package kr.osj.livving.domain.greeting.di

import kr.osj.livving.domain.greeting.GetGreetingUseCase
import org.koin.dsl.module

val greetingDomainModule = module {
    factory { GetGreetingUseCase(get()) }
}
