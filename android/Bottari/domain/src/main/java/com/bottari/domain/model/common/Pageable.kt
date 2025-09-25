package com.bottari.domain.model.common

data class Pageable<T>(
    val contents: List<T> = emptyList(),
    val currentPage: Int = -1,
    val hasNext: Boolean = true,
    val lastId: Long? = null,
    val lastInfo: String? = null,
) {
    // TODO: 임시 처리 > 서버한테 lastInfo 수정 필요하다고 전달 필요
    val realLastInfo: String = lastInfo?.substringBefore('.') ?: ""

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
