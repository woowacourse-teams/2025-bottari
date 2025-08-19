package com.bottari.di

import com.bottari.data.local.notification.NotificationDatabase

object DatabaseProvider {
    val notificationDatabase: NotificationDatabase by lazy {
        NotificationDatabase.getDatabase(ApplicationContextProvider.applicationContext)
    }
}
