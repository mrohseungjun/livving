package kr.osj.livving.domain.livving.di

import kr.osj.livving.domain.livving.usecase.AcceptGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.CompleteCheckInUseCase
import kr.osj.livving.domain.livving.usecase.CreateGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.DisconnectGuardianUseCase
import kr.osj.livving.domain.livving.usecase.GetActiveInviteLinksUseCase
import kr.osj.livving.domain.livving.usecase.GetCurrentAuthSessionUseCase
import kr.osj.livving.domain.livving.usecase.GetGuardianInviteRequestUseCase
import kr.osj.livving.domain.livving.usecase.GetMyGuardiansUseCase
import kr.osj.livving.domain.livving.usecase.GetNotificationsUseCase
import kr.osj.livving.domain.livving.usecase.GetTodayCheckInUseCase
import kr.osj.livving.domain.livving.usecase.GetWatchingUsersUseCase
import kr.osj.livving.domain.livving.usecase.LoginWithKakaoUseCase
import kr.osj.livving.domain.livving.usecase.MarkNotificationReadUseCase
import kr.osj.livving.domain.livving.usecase.RegisterPushTokenUseCase
import kr.osj.livving.domain.livving.usecase.SaveInitialUserSettingsUseCase
import kr.osj.livving.domain.livving.usecase.SavePhoneContactUseCase
import kr.osj.livving.domain.livving.usecase.ToggleLateCheckInUseCase
import org.koin.dsl.module

val livvingDomainModule = module {
    factory { CompleteCheckInUseCase(get()) }
    factory { ToggleLateCheckInUseCase() }
    factory { CreateGuardianInviteUseCase(get()) }
    factory { GetMyGuardiansUseCase(get()) }
    factory { GetWatchingUsersUseCase(get()) }
    factory { GetActiveInviteLinksUseCase(get()) }
    factory { GetGuardianInviteRequestUseCase(get()) }
    factory { AcceptGuardianInviteUseCase(get()) }
    factory { DisconnectGuardianUseCase(get()) }
    factory { LoginWithKakaoUseCase(get(), get()) }
    factory { GetCurrentAuthSessionUseCase(get()) }
    factory { GetTodayCheckInUseCase(get()) }
    factory { SaveInitialUserSettingsUseCase(get()) }
    factory { SavePhoneContactUseCase(get()) }
    factory { RegisterPushTokenUseCase(get()) }
    factory { GetNotificationsUseCase(get()) }
    factory { MarkNotificationReadUseCase(get()) }
}
