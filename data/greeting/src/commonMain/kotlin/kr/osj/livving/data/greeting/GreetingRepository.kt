package kr.osj.livving.data.greeting

import kr.osj.livving.core.platform.getPlatform

class GreetingRepository(
    private val platformName: () -> String = { getPlatform().name },
) {
    fun greeting(): String = "Hello, ${platformName()}!"
}
