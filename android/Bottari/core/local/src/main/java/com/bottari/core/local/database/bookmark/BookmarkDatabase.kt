package com.bottari.core.local.database.bookmark

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bottari.core.local.database.converter.StringListJsonConverter
import com.bottari.core.local.entity.bookmark.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1)
@TypeConverters(StringListJsonConverter::class)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        private const val DATABASE_NAME = "bookmark_database"

        fun create(context: Context): BookmarkDatabase =
            Room
                .databaseBuilder(
                    context = context.applicationContext,
                    klass = BookmarkDatabase::class.java,
                    name = DATABASE_NAME,
                ).build()
    }
}
