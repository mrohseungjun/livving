package kr.osj.livving.feature.splash.di

import kr.osj.livving.feature.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val splashFeatureModule = module {
    viewModel { SplashViewModel() }
}
