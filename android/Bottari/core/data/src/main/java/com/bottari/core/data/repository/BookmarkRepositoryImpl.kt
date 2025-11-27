package com.bottari.core.data.repository

import com.bottari.core.data.source.local.BookmarkLocalDataSource
import com.bottari.core.domain.model.bottari.template.BookmarkTemplate
import com.bottari.core.domain.repository.BookmarkRepository
import com.bottari.core.local.entity.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val localDataSource: BookmarkLocalDataSource,
) : BookmarkRepository {
    override fun observeAllBookmarks(): Flow<List<BookmarkTemplate>> =
        localDataSource.observeAllBookmarks().map { entities ->
            entities.map { entity -> entity.toBookmarkTemplate() }
        }

    override suspend fun upsertBookmark(bookmark: BookmarkTemplate): Result<Unit> =
        runCatching { localDataSource.upsertBookmark(BookmarkEntity.fromBookmarkTemplate(bookmark)) }

    override suspend fun deleteBookmarkByTemplateId(templateId: Long): Result<Unit> =
        runCatching { localDataSource.deleteBookmarkByTemplateId(templateId) }

    override suspend fun getBookmarkByTemplateId(templateId: Long): Result<BookmarkTemplate?> =
        runCatching { localDataSource.getBookmarkByTemplateId(templateId)?.toBookmarkTemplate() }

    override suspend fun existsByTemplateId(templateId: Long): Result<Boolean> =
        runCatching { localDataSource.existsByTemplateId(templateId) }
}
