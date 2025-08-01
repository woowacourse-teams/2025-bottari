package com.bottari.bottari

import android.app.Application
import com.bottari.di.ApplicationContextProvider
import timber.log.Timber

class BottariApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        ApplicationContextProvider.init(this)
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
