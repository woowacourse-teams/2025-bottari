package com.bottari.logger

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

object BottariLogger {
    fun init(
        context: Context,
        userId: String,
    ) {
        val analytics = FirebaseAnalytics.getInstance(context)
        val crashlytics = FirebaseCrashlytics.getInstance()
        val bottariTree = BottariTree(userId, analytics, crashlytics)
        Timber.plant(bottariTree)
    }

    fun log(
        tag: String,
        message: String,
    ) = Timber.tag(tag).i(message)

    fun ui(message: String) = log(LogLevel.UI, message)

    fun data(message: String) = log(LogLevel.DATA, message)

    fun network(message: String) = log(LogLevel.NETWORK, message)

    fun global(message: String) = log(LogLevel.GLOBAL, message)

    fun debug(message: String) = log(LogLevel.DEBUG, message)

    fun lifecycle(message: String) = log(LogLevel.LIFECYCLE, message)

    fun error(message: String) = log(LogLevel.ERROR, message)

    fun error(
        message: String?,
        throwable: Throwable,
    ) {
        message.orEmpty().ifEmpty { Timber.tag(LogLevel.ERROR.label).e(throwable) }
        Timber.tag(LogLevel.ERROR.label).e(throwable, message)
    }

    private fun log(
        level: LogLevel,
        message: String,
    ) {
        Timber.tag(level.label).log(level.priority, message)
    }
}
