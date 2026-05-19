package kr.osj.livving.domain.greeting

import kotlin.test.Test
import kotlin.test.assertEquals

class GetGreetingUseCaseTest {
    @Test
    fun returnsGreetingFromRepository() {
        val useCase = GetGreetingUseCase(
            object : GreetingRepository {
                override fun greeting(): String = "Hello, Test!"
            },
        )

        assertEquals("Hello, Test!", useCase())
    }
}
