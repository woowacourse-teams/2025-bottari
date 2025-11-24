package com.bottari.core.domain.usecase.template

import com.bottari.core.domain.model.bottari.template.BottariTemplate
import com.bottari.core.domain.model.common.Pageable
import com.bottari.core.domain.repository.BookmarkRepository
import com.bottari.core.domain.repository.BottariTemplateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import javax.inject.Inject

class SearchTemplatesByHashtagUseCase @Inject constructor(
    private val bottariTemplateRepository: BottariTemplateRepository,
    private val bookmarkRepository: BookmarkRepository,
) {
    suspend operator fun invoke(
        hashtagId: Long,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> =
        bottariTemplateRepository
            .searchTemplatesByHashtag(
                hashtagId = hashtagId,
                pageable = pageable,
            ).mapCatching { result ->
                val newContent = applyBookmarkStatusesParallel(result.contents)
                val newPageable = result.copy(contents = newContent)
                pageable.merge(newPageable)
            }

    private suspend fun applyBookmarkStatusesParallel(
        templates: List<BottariTemplate>,
        parallelism: Int = 8,
    ): List<BottariTemplate> =
        coroutineScope {
            val semaphore = Semaphore(parallelism)
            templates.map { template -> temp(semaphore, template) }.awaitAll()
        }

    private fun CoroutineScope.temp(
        semaphore: Semaphore,
        template: BottariTemplate,
    ): Deferred<BottariTemplate> =
        async {
            semaphore.withPermit {
                bookmarkRepository
                    .existsByTemplateId(template.id)
                    .getOrElse { false }
                    .let { isMarked -> template.copy(isMarked = isMarked) }
            }
        }
}
