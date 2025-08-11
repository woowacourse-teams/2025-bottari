package com.bottari.data.local.notification

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bottari.data.common.util.LocalDateConverter
import com.bottari.data.common.util.LocalTimeConverter
import com.bottari.data.common.util.RepeatDaysConverter
import com.bottari.data.model.notification.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
@TypeConverters(LocalTimeConverter::class, LocalDateConverter::class, RepeatDaysConverter::class)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

    companion object {
        private const val DATABASE_NAME = "Notification"

        @Volatile
        private var instance: NotificationDatabase? = null

        fun getDatabase(context: Context): NotificationDatabase =
            instance ?: synchronized(this) {
                Room
                    .databaseBuilder(
                        context,
                        NotificationDatabase::class.java,
                        DATABASE_NAME,
                    ).build()
                    .also { instance = it }
            }
    }
}
