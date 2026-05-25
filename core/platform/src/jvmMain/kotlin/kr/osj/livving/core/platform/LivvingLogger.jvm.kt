package kr.osj.livving.core.platform

import java.util.logging.Logger

actual fun livvingLogD(tag: String, message: String) {
    Logger.getLogger(tag).fine(message)
}
