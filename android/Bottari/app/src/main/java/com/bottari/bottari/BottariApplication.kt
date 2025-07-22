package com.bottari.bottari

import android.app.Application
import timber.log.Timber

class BottariApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
