package com.bottari.data.local.tooltip

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bottari.data.model.local.tooltip.TooltipEntity

@Database(
    entities = [
        TooltipEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class TooltipDatabase : RoomDatabase() {
    abstract fun tooltipDao(): TooltipDismissalDao

    companion object {
        private const val DATABASE_NAME = "tooltip"

        @Volatile
        private var instance: TooltipDatabase? = null

        fun getDatabase(context: Context): TooltipDatabase =
            instance ?: synchronized(this) {
                instance ?: Room
                    .databaseBuilder(
                        context.applicationContext,
                        TooltipDatabase::class.java,
                        DATABASE_NAME,
                    ).build()
                    .also { instance = it }
            }
    }
}
