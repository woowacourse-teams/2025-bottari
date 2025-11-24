package com.bottari.core.network.dto.common

import com.bottari.core.domain.model.common.Pageable

data class PageableRequest(
    val lastId: Long? = null,
    val lastInfo: String? = null,
    val page: Int = 0,
    val size: Int = 15,
    val property: String = "createdAt",
) {
    fun toQueryMap(): Map<String, String> =
        buildMap {
            lastId?.let { put("lastId", it.toString()) }
            lastInfo?.let { put("lastInfo", it) }
            put("page", page.toString())
            put("size", this@PageableRequest.size.toString())
            put("property", property)
        }

    companion object {
        fun of(pageable: Pageable<*>): PageableRequest =
            PageableRequest(
                lastId = pageable.lastId,
                lastInfo = pageable.lastInfo,
                page = pageable.currentPage,
            )
    }
}
