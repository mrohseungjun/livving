package kr.osj.livving.core.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

actual fun platformClipboardClient(): ClipboardClient = AndroidClipboardClient

private object AndroidClipboardClient : ClipboardClient {
    override fun copyText(text: String) {
        val context = AndroidTextShareContext.requireContext()
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("livving", text))
    }
}
