package com.bottari.presentation.util

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsLogger {
    private const val KEY_CURRENT_SCREEN = "current_screen"
    private const val KEY_PARENT_ACTIVITY = "current_parent_activity"

    private const val PREFIX_LOG = "[LOG] %s"
    private const val PREFIX_EXCEPTION = "[EXCEPTION] %s"
    private const val PREFIX_ACTIVITY_SCREEN = "[SET_ACTIVITY_SCREEN] %s"
    private const val PREFIX_FRAGMENT_SCREEN = "[SET_FRAGMENT_SCREEN] %s"

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    fun log(message: String) {
        crashlytics.log(PREFIX_LOG.format(message))
    }

    fun recordException(throwable: Throwable) {
        with(crashlytics) {
            log(PREFIX_EXCEPTION.format(throwable.message.orEmpty()))
            recordException(throwable)
        }
    }

    fun setScreen(activity: Activity) {
        val className = activity::class.java.simpleName
        with(crashlytics) {
            log(PREFIX_ACTIVITY_SCREEN.format(className))
            setCustomKey(KEY_CURRENT_SCREEN, className)
        }
    }

    fun setScreen(fragment: Fragment) {
        val fragmentName = fragment::class.java.simpleName
        val parentName = fragment.activity?.javaClass?.simpleName

        with(crashlytics) {
            log(PREFIX_FRAGMENT_SCREEN.format("$parentName / $fragmentName"))
            setCustomKey(KEY_CURRENT_SCREEN, fragmentName)
            parentName?.let { setCustomKey(KEY_PARENT_ACTIVITY, it) }
        }
    }
}
