package kr.osj.livving.feature.relations.di

import kr.osj.livving.feature.relations.GuardianDetailViewModel
import kr.osj.livving.feature.relations.InviteStatusViewModel
import kr.osj.livving.feature.relations.InviteViewModel
import kr.osj.livving.feature.relations.RelationsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val relationsFeatureModule = module {
    viewModel { RelationsViewModel() }
    viewModel { InviteViewModel() }
    viewModel { InviteStatusViewModel() }
    viewModel { GuardianDetailViewModel() }
}
