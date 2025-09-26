package com.bottari.data.model.remote.common

import com.bottari.domain.model.common.Pageable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageableResponse<T>(
    @SerialName("contents")
    val contents: List<T>,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("first")
    val first: Boolean,
    @SerialName("last")
    val last: Boolean,
    @SerialName("sortProperty")
    val sortProperty: String,
    @SerialName("lastId")
    val lastId: Long?,
    @SerialName("lastInfo")
    val lastInfo: String?,
) {
    fun <R> toDomain(mapper: (T) -> R): Pageable<R> =
        Pageable(
            contents = contents.map(mapper),
            currentPage = currentPage,
            hasNext = hasNext,
            lastId = lastId,
            lastInfo = lastInfo,
        )
}
