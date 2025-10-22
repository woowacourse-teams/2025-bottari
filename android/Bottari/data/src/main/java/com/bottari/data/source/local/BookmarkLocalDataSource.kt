package com.bottari.data.source.local

import com.bottari.data.model.local.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkLocalDataSource {
    fun observeAllBookmarks(): Flow<List<BookmarkEntity>>

    suspend fun upsertBookmark(bookmark: BookmarkEntity)

    suspend fun deleteBookmarkByTemplateId(templateId: Long)

    suspend fun getBookmarkById(id: Long): BookmarkEntity?

    suspend fun existsByTemplateId(templateId: Long): Boolean
}
