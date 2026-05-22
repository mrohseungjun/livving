package kr.osj.livving.core.platform

interface ClipboardClient {
    fun copyText(text: String)
}

expect fun platformClipboardClient(): ClipboardClient
