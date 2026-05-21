package kr.osj.livving.auth

import android.content.Context

object AndroidKakaoAuthContext {
    private var context: Context? = null

    val applicationContext: Context
        get() = requireNotNull(context) {
            "AndroidKakaoAuthContext must be initialized before starting Koin."
        }

    fun initialize(context: Context) {
        this.context = context.applicationContext
    }
}
