package kr.osj.livving.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
