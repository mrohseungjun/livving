package kr.osj.livving.di

import kr.osj.livving.data.network.di.networkModule
import kr.osj.livving.domain.livving.di.livvingDomainModule
import kr.osj.livving.feature.main.di.mainFeatureModule
import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        livvingDomainModule,
        mainFeatureModule,
        platformAuthModule,
    )
}
