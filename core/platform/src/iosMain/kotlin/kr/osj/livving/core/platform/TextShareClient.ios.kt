package kr.osj.livving.core.platform

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual fun platformTextShareClient(): TextShareClient = IosTextShareClient

private object IosTextShareClient : TextShareClient {
    override fun shareText(text: String, title: String) {
        val controller = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null,
        )
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            viewControllerToPresent = controller,
            animated = true,
            completion = null,
        )
    }
}
