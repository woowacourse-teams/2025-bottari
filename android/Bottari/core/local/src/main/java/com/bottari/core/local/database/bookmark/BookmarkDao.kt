package com.bottari.core.local.database.bookmark

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bottari.core.local.entity.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Upsert
    suspend fun upsertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmark WHERE templateId = :templateId")
    suspend fun deleteBookmarkByTemplateId(templateId: Long)

    @Query("SELECT * FROM bookmark ORDER BY createdAt ASC")
    fun observeAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmark WHERE templateId = :templateId LIMIT 1")
    suspend fun getBookmarkByTemplateId(templateId: Long): BookmarkEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM bookmark WHERE templateId = :templateId)")
    suspend fun existsByTemplateId(templateId: Long): Boolean
}
