package kr.osj.livving.domain.greeting

import kr.osj.livving.data.greeting.GreetingRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetGreetingUseCaseTest {
    @Test
    fun returnsGreetingFromRepository() {
        val useCase = GetGreetingUseCase(GreetingRepository { "Test" })

        assertEquals("Hello, Test!", useCase())
    }
}
