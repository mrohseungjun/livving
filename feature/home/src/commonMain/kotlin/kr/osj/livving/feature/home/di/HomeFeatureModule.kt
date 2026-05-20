package kr.osj.livving.feature.home.di

import kr.osj.livving.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeFeatureModule = module {
    viewModel { HomeViewModel() }
}
