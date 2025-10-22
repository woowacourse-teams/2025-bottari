package com.bottari.data.source.local

import com.bottari.data.local.bookmark.BookmarkDao
import com.bottari.data.model.local.bookmark.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkLocalDataSourceImpl @Inject constructor(
    private val dao: BookmarkDao,
) : BookmarkLocalDataSource {
    override fun observeAllBookmarks(): Flow<List<BookmarkEntity>> = dao.observeAllBookmarks()

    override suspend fun upsertBookmark(bookmark: BookmarkEntity) = dao.upsertBookmark(bookmark)

    override suspend fun deleteBookmarkByTemplateId(templateId: Long) = dao.deleteBookmarkByTemplateId(templateId)

    override suspend fun getBookmarkByTemplateId(templateId: Long): BookmarkEntity? = dao.getBookmarkByTemplateId(templateId)

    override suspend fun existsByTemplateId(templateId: Long): Boolean = dao.existsByTemplateId(templateId)
}
