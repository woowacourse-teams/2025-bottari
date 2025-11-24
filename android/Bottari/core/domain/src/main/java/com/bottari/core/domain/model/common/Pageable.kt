package com.bottari.core.domain.model.common

data class Pageable<T>(
    val contents: List<T> = emptyList(),
    val currentPage: Int = -1,
    val hasNext: Boolean = true,
    val lastId: Long? = null,
    val lastInfo: String? = null,
) {
    fun nextRequest(): Pageable<T> = copy(currentPage = currentPage + 1)

    fun merge(next: Pageable<T>): Pageable<T> =
        copy(
            contents = (contents + next.contents).distinct(),
            currentPage = next.currentPage,
            hasNext = next.hasNext,
            lastId = next.lastId,
            lastInfo = next.lastInfo,
        )
}
