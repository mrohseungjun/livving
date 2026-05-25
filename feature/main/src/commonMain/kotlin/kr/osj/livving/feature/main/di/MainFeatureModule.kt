package kr.osj.livving.feature.main.di

import kr.osj.livving.feature.auth.di.authFeatureModule
import kr.osj.livving.feature.home.di.homeFeatureModule
import kr.osj.livving.feature.main.MainViewModel
import kr.osj.livving.feature.notifications.di.notificationsFeatureModule
import kr.osj.livving.feature.relations.di.relationsFeatureModule
import kr.osj.livving.feature.splash.di.splashFeatureModule
import kr.osj.livving.feature.settings.di.settingsFeatureModule
import kr.osj.livving.feature.setup.di.setupFeatureModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainFeatureModule = module {
    includes(
        authFeatureModule,
        homeFeatureModule,
        notificationsFeatureModule,
        relationsFeatureModule,
        splashFeatureModule,
        settingsFeatureModule,
        setupFeatureModule,
    )

    viewModel {
        MainViewModel(
            completeCheckInUseCase = get(),
            toggleLateCheckInUseCase = get(),
            createGuardianInviteUseCase = get(),
            getMyGuardiansUseCase = get(),
            getActiveInviteLinksUseCase = get(),
            getGuardianInviteRequestUseCase = get(),
            getGuardianInviteRequestByOwnerUseCase = get(),
            acceptGuardianInviteUseCase = get(),
            disconnectGuardianUseCase = get(),
            getWatchingUsersUseCase = get(),
            getCurrentAuthSessionUseCase = get(),
            getTodayCheckInUseCase = get(),
            saveInitialUserSettingsUseCase = get(),
            savePhoneContactUseCase = get(),
            registerPushTokenUseCase = get(),
            getNotificationsUseCase = get(),
            markNotificationReadUseCase = get(),
            logoutUseCase = get(),
        )
    }
}
