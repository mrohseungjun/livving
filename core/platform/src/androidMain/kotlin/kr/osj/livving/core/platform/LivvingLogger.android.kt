package kr.osj.livving.core.platform

import timber.log.Timber

actual fun livvingLogD(tag: String, message: String) {
    if (Timber.treeCount == 0) {
        Timber.plant(Timber.DebugTree())
    }
    Timber.tag(tag).d(message)
}
