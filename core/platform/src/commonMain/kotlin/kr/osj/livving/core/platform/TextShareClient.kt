package kr.osj.livving.core.platform

interface TextShareClient {
    fun shareText(text: String, title: String = "livving")
}

expect fun platformTextShareClient(): TextShareClient
