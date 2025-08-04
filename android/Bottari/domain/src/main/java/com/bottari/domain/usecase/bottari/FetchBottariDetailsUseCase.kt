package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.repository.BottariRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class FetchBottariDetailsUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(ssaid: String): Result<List<BottariDetail>> =
        runCatching {
            val bottaries = bottariRepository.fetchBottaries(ssaid).getOrThrow()
            fetchBottariDetailsWithItems(ssaid, bottaries)
        }

    private suspend fun fetchBottariDetailsWithItems(
        ssaid: String,
        bottaries: List<Bottari>,
    ): List<BottariDetail> =
        supervisorScope {
            bottaries
                .map { bottari -> async { runCatching { fetchBottariItem(ssaid, bottari.id) }.getOrNull() } }
                .mapNotNull { it.await() }
        }

    private suspend fun fetchBottariItem(
        ssaid: String,
        bottariId: Long,
    ): BottariDetail? {
        val result = bottariRepository.fetchBottariDetail(bottariId, ssaid).getOrNull() ?: return null
        if (result.items.isEmpty()) return null

        return BottariDetail(
            id = result.id,
            title = result.title,
            alarm = null,
            items = result.items,
        )
    }
}
