package kr.osj.livving.feature.setup.di

import kr.osj.livving.feature.setup.DeadlineViewModel
import kr.osj.livving.feature.setup.DelayViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val setupFeatureModule = module {
    viewModel { DeadlineViewModel() }
    viewModel { DelayViewModel() }
}
