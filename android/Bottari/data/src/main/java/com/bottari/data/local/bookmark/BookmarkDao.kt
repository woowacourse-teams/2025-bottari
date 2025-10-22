package com.bottari.data.local.bookmark

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bottari.data.model.local.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Upsert
    suspend fun upsertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmark WHERE templateId = :templateId")
    suspend fun deleteBookmarkByTemplateId(templateId: Long)

    @Query("SELECT * FROM bookmark ORDER BY createdAt DESC")
    fun observeAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmark WHERE id = :id LIMIT 1")
    suspend fun getBookmarkById(id: Long): BookmarkEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM bookmark WHERE templateId = :templateId)")
    suspend fun existsByTemplateId(templateId: Long): Boolean
}
