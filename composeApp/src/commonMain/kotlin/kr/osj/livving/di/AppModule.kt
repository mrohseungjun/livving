package kr.osj.livving.di

import kr.osj.livving.data.greeting.di.greetingDataModule
import kr.osj.livving.feature.greeting.di.greetingFeatureModule
import org.koin.dsl.module

val appModule = module {
    includes(
        greetingDataModule,
        greetingFeatureModule,
    )
}
