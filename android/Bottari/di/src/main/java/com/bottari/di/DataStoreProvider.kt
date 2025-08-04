package com.bottari.di

import com.bottari.data.local.AppConfigDataStore

object DataStoreProvider {
    val provideAppConfigDataStore: AppConfigDataStore by lazy {
        AppConfigDataStore(ApplicationContextProvider.applicationContext)
    }
}
