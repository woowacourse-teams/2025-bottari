package com.bottari.di

import com.bottari.data.local.bottari.BottariDatabase
import com.bottari.data.local.notification.NotificationDatabase

object DatabaseProvider {
    val notificationDatabase: NotificationDatabase by lazy {
        NotificationDatabase.getDatabase(ApplicationContextProvider.applicationContext)
    }

    val bottariDatabase: BottariDatabase by lazy {
        BottariDatabase.getDatabase(ApplicationContextProvider.applicationContext)
    }
}
