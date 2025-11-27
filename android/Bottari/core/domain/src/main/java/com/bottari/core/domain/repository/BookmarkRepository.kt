package com.bottari.core.domain.repository

import com.bottari.core.domain.model.bottari.template.BookmarkTemplate
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun observeAllBookmarks(): Flow<List<BookmarkTemplate>>

    suspend fun upsertBookmark(bookmark: BookmarkTemplate): Result<Unit>

    suspend fun deleteBookmarkByTemplateId(templateId: Long): Result<Unit>

    suspend fun getBookmarkByTemplateId(templateId: Long): Result<BookmarkTemplate?>

    suspend fun existsByTemplateId(templateId: Long): Result<Boolean>
}
