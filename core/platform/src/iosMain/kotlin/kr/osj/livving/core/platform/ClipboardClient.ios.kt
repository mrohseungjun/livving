package kr.osj.livving.core.platform

import platform.UIKit.UIPasteboard

actual fun platformClipboardClient(): ClipboardClient = IosClipboardClient

private object IosClipboardClient : ClipboardClient {
    override fun copyText(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}
