package kr.osj.livving.domain.greeting

class GetGreetingUseCase(
    private val repository: GreetingRepository,
) {
    operator fun invoke(): String = repository.greeting()
}
