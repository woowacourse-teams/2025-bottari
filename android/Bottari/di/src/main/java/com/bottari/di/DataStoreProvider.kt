package com.bottari.di

import com.bottari.bottari.BottariApplication
import com.bottari.data.local.AppConfigDataStore

object DataStoreProvider {
    val provideAppConfigDataStore: AppConfigDataStore by lazy {
        AppConfigDataStore(BottariApplication.instance)
    }
}
