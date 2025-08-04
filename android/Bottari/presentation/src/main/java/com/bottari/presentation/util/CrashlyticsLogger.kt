package com.bottari.presentation.util

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

object CrashlyticsLogger {
    private const val TAG = "CrashlyticsLogger"
    private const val KEY_CURRENT_SCREEN = "current_screen"
    private const val KEY_PARENT_ACTIVITY = "current_parent_activity"

    private const val PREFIX_SET_USER_ID = "[SET_USER_ID] %s"
    private const val PREFIX_LOG = "[LOG] %s"
    private const val PREFIX_EXCEPTION = "[EXCEPTION] %s"
    private const val PREFIX_ACTIVITY_SET_SCREEN = "[SET_ACTIVITY_SCREEN] %s"
    private const val PREFIX_FRAGMENT_SET_SCREEN = "[SET_FRAGMENT_SCREEN] %s"

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    fun setUserId(userId: String) {
        val formatted = PREFIX_SET_USER_ID.format(userId)
        Timber.tag(TAG).d(formatted)
        crashlytics.setUserId(userId)
    }

    fun log(message: String) {
        val formatted = PREFIX_LOG.format(message)
        logAndSendToCrashlytics(formatted)
    }

    fun recordException(throwable: Throwable) {
        val formatted = PREFIX_EXCEPTION.format(throwable.message.orEmpty())
        Timber.tag(TAG).i(formatted)
        crashlytics.log(formatted)
        crashlytics.recordException(throwable)
        Timber.tag(TAG).d("")
    }

    fun setScreen(activity: Activity) {
        val className = activity::class.java.simpleName
        val formatted = PREFIX_ACTIVITY_SET_SCREEN.format(className)
        logAndSendToCrashlytics(formatted)
        crashlytics.setCustomKey(KEY_CURRENT_SCREEN, className)
    }

    fun setScreen(fragment: Fragment) {
        val className = fragment::class.java.simpleName
        val parentClassName = fragment.activity?.javaClass?.simpleName
        val formatted = PREFIX_FRAGMENT_SET_SCREEN.format("$parentClassName / $className")

        logAndSendToCrashlytics(formatted)

        crashlytics.setCustomKey(KEY_CURRENT_SCREEN, className)
        parentClassName?.let {
            crashlytics.setCustomKey(KEY_PARENT_ACTIVITY, it)
        }
    }

    private fun logAndSendToCrashlytics(message: String) {
        Timber.tag(TAG).d(message)
        crashlytics.log(message)
    }
}
