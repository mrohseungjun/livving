package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.AuthSession
import kr.osj.livving.domain.livving.repository.AuthRepository

class GetCurrentAuthSessionUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): AuthSession? = repository.getCurrentSession()
}
