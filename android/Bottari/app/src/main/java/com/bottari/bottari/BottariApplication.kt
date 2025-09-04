package com.bottari.bottari

import android.app.Application
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.bottari.di.ApplicationContextProvider
import com.bottari.di.usecase.CommonUseCaseProvider
import com.bottari.logger.BottariLogger
import com.bottari.presentation.worker.NotificationWorkerFactory

class BottariApplication :
    Application(),
    Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        BottariLogger.init(this)
        ApplicationContextProvider.init(this)
        BottariLogger.global(APPLICATION_INIT_MESSAGE)
    }

    override val workManagerConfiguration: Configuration
        get() {
            val workerFactory =
                DelegatingWorkerFactory().apply {
                    addFactory(
                        NotificationWorkerFactory(
                            getNotificationsUseCase = CommonUseCaseProvider.getNotificationsUseCase,
                        ),
                    )
                }
            return Configuration
                .Builder()
                .setWorkerFactory(workerFactory)
                .build()
        }

    companion object {
        private const val APPLICATION_INIT_MESSAGE = "BOTTARI APPLICATION INIT"
    }
}
