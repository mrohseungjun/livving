package kr.osj.livving.core.platform

import platform.Foundation.NSLog

actual fun livvingLogD(tag: String, message: String) {
    NSLog("[$tag] $message")
}
