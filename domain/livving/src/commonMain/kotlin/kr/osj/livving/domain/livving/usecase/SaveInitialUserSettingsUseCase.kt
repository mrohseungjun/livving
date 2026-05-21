package kr.osj.livving.domain.livving.usecase

import kr.osj.livving.domain.livving.InitialUserSettings
import kr.osj.livving.domain.livving.repository.AuthRepository

class SaveInitialUserSettingsUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(userId: String, settings: InitialUserSettings) {
        repository.saveInitialSettings(userId, settings)
    }
}
