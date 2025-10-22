package com.bottari.data.local.bookmark

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bottari.data.model.local.bookmark.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1)
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
