package kr.osj.livving.core.platform

import android.content.Context
import android.content.Intent

object AndroidTextShareContext {
    private var context: Context? = null

    fun initialize(context: Context) {
        this.context = context.applicationContext
    }

    internal fun requireContext(): Context {
        return checkNotNull(context) { "AndroidTextShareContext is not initialized" }
    }
}

actual fun platformTextShareClient(): TextShareClient = AndroidTextShareClient

private object AndroidTextShareClient : TextShareClient {
    override fun shareText(text: String, title: String) {
        val context = AndroidTextShareContext.requireContext()
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(sendIntent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
