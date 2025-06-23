package com.arch.example.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

object IntentUtils {
    fun openLink(context: Context, url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        return try {
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}
