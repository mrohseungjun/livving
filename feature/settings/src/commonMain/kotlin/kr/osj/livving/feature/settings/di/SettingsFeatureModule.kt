package kr.osj.livving.feature.settings.di

import kr.osj.livving.feature.settings.HistoryViewModel
import kr.osj.livving.feature.settings.PrivacyViewModel
import kr.osj.livving.feature.settings.ProfileViewModel
import kr.osj.livving.feature.settings.ScheduleViewModel
import kr.osj.livving.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsFeatureModule = module {
    viewModel { SettingsViewModel() }
    viewModel { ScheduleViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { PrivacyViewModel() }
    viewModel { HistoryViewModel() }
}
