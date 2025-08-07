package com.bottari.bottari

import android.app.Application
import com.bottari.di.AnalyticsUserIdProvider
import com.bottari.di.ApplicationContextProvider
import com.bottari.logger.BottariLogger

class BottariApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BottariLogger.init(this, AnalyticsUserIdProvider.getOrCreate(this))
        ApplicationContextProvider.init(this)
        BottariLogger.global(APPLICATION_INIT_MESSAGE)
    }

    companion object {
        private const val APPLICATION_INIT_MESSAGE = "BOTTARI APPLICATION INIT"
    }
}
