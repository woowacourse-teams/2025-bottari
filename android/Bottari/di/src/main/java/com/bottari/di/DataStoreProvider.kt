package com.bottari.di

import com.bottari.data.local.AppConfigDataStore
import com.bottari.data.local.UserInfoDataStore

object DataStoreProvider {
    val provideAppConfigDataStore: AppConfigDataStore by lazy {
        AppConfigDataStore(ApplicationContextProvider.applicationContext)
    }
    val provideUserInfoDataStore: UserInfoDataStore by lazy {
        UserInfoDataStore(ApplicationContextProvider.applicationContext)
    }
}
