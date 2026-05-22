package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.LivvingUser
import kr.osj.livving.domain.livving.repository.AuthRepository

class SavePhoneContactUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(userId: String, phoneNumber: String?, phoneCallEnabled: Boolean): LivvingUser {
        return repository.savePhoneContact(userId, phoneNumber, phoneCallEnabled)
    }
}
