package com.bottari.bottari

import android.app.Application
import timber.log.Timber

class BottariApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
        _instance = this
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        private var _instance: BottariApplication? = null
        val instance: BottariApplication = _instance!!
    }
}
