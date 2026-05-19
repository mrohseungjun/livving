package kr.osj.livving.domain.greeting

import kr.osj.livving.data.greeting.GreetingRepository

class GetGreetingUseCase(
    private val repository: GreetingRepository = GreetingRepository(),
) {
    operator fun invoke(): String = repository.greeting()
}
