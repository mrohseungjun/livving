package kr.osj.livving.core.platform

actual fun platformClipboardClient(): ClipboardClient = JvmClipboardClient

private object JvmClipboardClient : ClipboardClient {
    override fun copyText(text: String) = Unit
}
