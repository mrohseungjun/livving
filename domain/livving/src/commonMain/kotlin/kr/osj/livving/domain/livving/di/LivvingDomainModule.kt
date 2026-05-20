package kr.osj.livving.domain.livving.di

import kr.osj.livving.domain.livving.usecase.CompleteCheckInUseCase
import kr.osj.livving.domain.livving.usecase.CreateGuardianInviteUseCase
import kr.osj.livving.domain.livving.usecase.ToggleLateCheckInUseCase
import org.koin.dsl.module

val livvingDomainModule = module {
    factory { CompleteCheckInUseCase() }
    factory { ToggleLateCheckInUseCase() }
    factory { CreateGuardianInviteUseCase() }
}
