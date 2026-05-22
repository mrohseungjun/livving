package kr.osj.livving.core.platform

actual fun platformTextShareClient(): TextShareClient = JvmTextShareClient

private object JvmTextShareClient : TextShareClient {
    override fun shareText(text: String, title: String) = Unit
}
