package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.repository.BottariRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class FetchBottariDetailsUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(): Result<List<BottariDetail>> =
        runCatching {
            val bottaries = bottariRepository.fetchBottaries().getOrThrow()
            fetchBottariDetailsWithItems(bottaries)
        }

    private suspend fun fetchBottariDetailsWithItems(bottaries: List<Bottari>): List<BottariDetail> =
        supervisorScope {
            bottaries
                .map { bottari -> async { runCatching { fetchBottariItem(bottari.id) }.getOrNull() } }
                .mapNotNull { it.await() }
        }

    private suspend fun fetchBottariItem(bottariId: Long): BottariDetail? {
        val result = bottariRepository.fetchBottariDetail(bottariId).getOrNull() ?: return null
        if (result.items.isEmpty()) return null

        return BottariDetail(
            id = result.id,
            title = result.title,
            alarm = null,
            items = result.items,
        )
    }
}
