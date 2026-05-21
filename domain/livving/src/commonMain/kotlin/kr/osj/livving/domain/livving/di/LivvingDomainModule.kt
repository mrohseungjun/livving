package kr.osj.livving.domain.livving.di

import kr.osj.livving.domain.livving.usecase.CompleteCheckInUseCase
import kr.osj.livving.domain.livving.usecase.CreateGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.GetCurrentAuthSessionUseCase
import kr.osj.livving.domain.livving.usecase.GetTodayCheckInUseCase
import kr.osj.livving.domain.livving.usecase.LoginWithKakaoUseCase
import kr.osj.livving.domain.livving.usecase.SaveInitialUserSettingsUseCase
import kr.osj.livving.domain.livving.usecase.ToggleLateCheckInUseCase
import org.koin.dsl.module

val livvingDomainModule = module {
    factory { CompleteCheckInUseCase(get()) }
    factory { ToggleLateCheckInUseCase() }
    factory { CreateGuardianInviteUseCase(get()) }
    factory { LoginWithKakaoUseCase(get(), get()) }
    factory { GetCurrentAuthSessionUseCase(get()) }
    factory { GetTodayCheckInUseCase(get()) }
    factory { SaveInitialUserSettingsUseCase(get()) }
}
