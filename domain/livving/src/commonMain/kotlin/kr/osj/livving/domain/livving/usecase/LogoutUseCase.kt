package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
