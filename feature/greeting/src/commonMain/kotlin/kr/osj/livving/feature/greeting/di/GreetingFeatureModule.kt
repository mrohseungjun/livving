package kr.osj.livving.feature.greeting.di

import kr.osj.livving.domain.greeting.di.greetingDomainModule
import kr.osj.livving.feature.greeting.GreetingViewModel
import org.koin.dsl.module

val greetingFeatureModule = module {
    includes(greetingDomainModule)
    factory { GreetingViewModel(get()) }
}
