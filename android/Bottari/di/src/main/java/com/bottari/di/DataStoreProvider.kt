package com.bottari.di

import com.bottari.data.local.AppConfigDataStore
import com.bottari.data.local.MemberInfoDataStore

object DataStoreProvider {
    val provideAppConfigDataStore: AppConfigDataStore by lazy {
        AppConfigDataStore(ApplicationContextProvider.applicationContext)
    }
    val provideMemberInfoDataStore: MemberInfoDataStore by lazy {
        MemberInfoDataStore(ApplicationContextProvider.applicationContext)
    }
}
