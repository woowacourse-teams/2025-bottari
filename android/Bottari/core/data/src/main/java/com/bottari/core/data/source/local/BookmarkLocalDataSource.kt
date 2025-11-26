package com.bottari.core.data.source.local

import com.bottari.core.local.entity.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkLocalDataSource {
    fun observeAllBookmarks(): Flow<List<BookmarkEntity>>

    suspend fun upsertBookmark(bookmark: BookmarkEntity)

    suspend fun deleteBookmarkByTemplateId(templateId: Long)

    suspend fun getBookmarkByTemplateId(templateId: Long): BookmarkEntity?

    suspend fun existsByTemplateId(templateId: Long): Boolean
}
