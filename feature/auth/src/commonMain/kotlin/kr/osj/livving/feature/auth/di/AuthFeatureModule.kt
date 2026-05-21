package kr.osj.livving.feature.auth.di

import kr.osj.livving.feature.auth.LoginViewModel
import kr.osj.livving.feature.auth.TermsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authFeatureModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { TermsViewModel() }
}
