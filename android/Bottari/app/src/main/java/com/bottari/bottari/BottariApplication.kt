package com.bottari.bottari

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.bottari.logger.BottariLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BottariApplication :
    Application(),
    Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(workerFactory)
                .build()

    override fun onCreate() {
        super.onCreate()
        BottariLogger.init(this)
        BottariLogger.global(APPLICATION_INIT_MESSAGE)
    }

    companion object {
        private const val APPLICATION_INIT_MESSAGE = "BOTTARI APPLICATION INIT"
    }
}
