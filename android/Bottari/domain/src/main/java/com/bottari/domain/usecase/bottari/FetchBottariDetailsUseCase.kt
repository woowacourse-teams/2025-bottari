package com.bottari.domain.usecase.bottari

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.getOrNull
import com.bottari.domain.model.exception.getOrThrow
import com.bottari.domain.model.exception.mapCatching
import com.bottari.domain.repository.BottariRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class FetchBottariDetailsUseCase(
    private val bottariRepository: BottariRepository,
) {
    suspend operator fun invoke(): BottariResult<List<Bottari>> =
        bottariRepository
            .fetchBottaries()
            .mapCatching { bottaries -> fetchBottariDetailsWithItems(bottaries) }

    private suspend fun fetchBottariDetailsWithItems(bottaries: List<BottariState>): List<Bottari> =
        supervisorScope {
            bottaries
                .map { bottariState -> async { runCatching { fetchBottariItem(bottariState.bottari.id) }.getOrNull() } }
                .mapNotNull { it.await() }
        }

    private suspend fun fetchBottariItem(bottariId: Long): Bottari? {
        val result = bottariRepository.fetchBottariDetail(bottariId).getOrNull() ?: return null
        if (result.items.isEmpty()) return null

        return Bottari(
            id = result.id,
            title = result.title,
            alarm = result.alarm,
            items = result.items,
        )
    }
}
