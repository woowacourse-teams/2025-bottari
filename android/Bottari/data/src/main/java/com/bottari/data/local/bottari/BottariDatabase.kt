package com.bottari.data.local.bottari

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.bottari.data.common.util.LocalDateConverter
import com.bottari.data.common.util.LocalTimeConverter
import com.bottari.data.common.util.RepeatDaysConverter
import com.bottari.data.model.local.bottari.AlarmEntity
import com.bottari.data.model.local.bottari.BottariEntity
import com.bottari.data.model.local.bottari.ItemEntity

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

    @Transaction
    suspend fun createBottariWithItems(
        bottari: BottariEntity,
        itemNames: List<String>,
    ): Long {
        val bottariId = bottariDao().createBottari(bottari)
        val items =
            Array(itemNames.size) { index ->
                ItemEntity.from(
                    bottariId = bottariId,
                    itemName = itemNames[index],
                )
            }
        itemDao().saveItem(*items)
        return bottariId
    }

    companion object {
        private const val DATABASE_NAME = "Bottari"

        @Volatile
        private var instance: BottariDatabase? = null

        fun getDatabase(context: Context): BottariDatabase =
            instance ?: synchronized(this) {
                instance ?: Room
                    .databaseBuilder(
                        context.applicationContext,
                        BottariDatabase::class.java,
                        DATABASE_NAME,
                    ).build()
                    .also { instance = it }
            }
    }
}
