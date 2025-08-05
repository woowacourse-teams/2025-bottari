package com.bottari.di

import android.content.Context

object ApplicationContextProvider {
    lateinit var applicationContext: Context
        private set

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext
    }
}
