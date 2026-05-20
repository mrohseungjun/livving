package kr.osj.livving.feature.notifications.di

import kr.osj.livving.feature.notifications.AlertViewModel
import kr.osj.livving.feature.notifications.NotificationsViewModel
import kr.osj.livving.feature.notifications.RequestViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val notificationsFeatureModule = module {
    viewModel { NotificationsViewModel() }
    viewModel { AlertViewModel() }
    viewModel { RequestViewModel() }
}
