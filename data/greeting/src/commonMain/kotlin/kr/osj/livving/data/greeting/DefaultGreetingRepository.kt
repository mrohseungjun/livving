package kr.osj.livving.data.greeting

import kr.osj.livving.core.platform.getPlatform
import kr.osj.livving.domain.greeting.GreetingRepository

class DefaultGreetingRepository(
    private val platformName: () -> String = { getPlatform().name },
) : GreetingRepository {
    override fun greeting(): String = "Hello, ${platformName()}!"
}
