package com.bottari.di

import com.bottari.data.local.bottari.BottariDatabase

object DatabaseProvider {
    val bottariDatabase: BottariDatabase by lazy {
        BottariDatabase.getDatabase(ApplicationContextProvider.applicationContext)
    }
}
