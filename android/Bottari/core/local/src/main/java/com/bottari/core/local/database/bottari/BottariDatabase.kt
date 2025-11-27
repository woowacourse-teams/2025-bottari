package com.bottari.core.local.database.bottari

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bottari.core.local.database.converter.LocalDateConverter
import com.bottari.core.local.database.converter.LocalTimeConverter
import com.bottari.core.local.database.converter.RepeatDaysConverter
import com.bottari.core.local.entity.bottari.AlarmEntity
import com.bottari.core.local.entity.bottari.BottariEntity
import com.bottari.core.local.entity.bottari.ItemEntity

@Database(
    entities = [
        BottariEntity::class,
        ItemEntity::class,
        AlarmEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(LocalTimeConverter::class, LocalDateConverter::class, RepeatDaysConverter::class)
abstract class BottariDatabase : RoomDatabase() {
    abstract fun bottariDao(): BottariDao

    abstract fun itemDao(): ItemDao

    abstract fun alarmDao(): AlarmDao

    companion object {
        private const val DATABASE_NAME = "Bottari"

        fun create(context: Context): BottariDatabase =
            Room
                .databaseBuilder(
                    context = context.applicationContext,
                    klass = BottariDatabase::class.java,
                    name = DATABASE_NAME,
                ).build()
    }
}
