package com.bottari.data.model.remote.common

import com.bottari.domain.model.common.Pageable

data class PageableRequest(
    val query: String? = null,
    val lastId: Long? = null,
    val lastInfo: String? = null,
    val page: Int = 0,
    val size: Int = 15,
    val property: String = "createdAt",
) {
    fun toQueryMap(): Map<String, String> =
        buildMap {
            query?.let { put("query", it) }
            lastId?.let { put("lastId", it.toString()) }
            lastInfo?.let { put("lastInfo", it) }
            put("page", page.toString())
            put("size", this@PageableRequest.size.toString())
            put("property", property)
        }

    companion object {
        fun of(
            query: String?,
            pageable: Pageable<*>,
        ): PageableRequest =
            PageableRequest(
                query = query,
                lastId = pageable.lastId,
                lastInfo = pageable.realLastInfo,
                page = pageable.currentPage,
            )
    }
}
