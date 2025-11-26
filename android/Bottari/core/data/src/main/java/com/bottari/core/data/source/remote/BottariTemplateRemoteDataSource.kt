package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.bottari.template.BottariTemplateCreateRequest
import com.bottari.core.network.dto.bottari.template.BottariTemplateCursorFetchResponse
import com.bottari.core.network.dto.bottari.template.BottariTemplateFetchResponse
import com.bottari.core.network.dto.common.PageableRequest
import com.bottari.core.network.dto.common.PageableResponse

interface BottariTemplateRemoteDataSource {
    suspend fun searchTemplatesByTitle(
        title: String,
        pageableRequest: PageableRequest,
    ): Result<PageableResponse<BottariTemplateCursorFetchResponse>>

    suspend fun searchTemplatesByHashtag(
        hashtagId: Long,
        pageableRequest: PageableRequest,
    ): Result<PageableResponse<BottariTemplateCursorFetchResponse>>

    suspend fun createBottariTemplate(bottariTemplateCreateRequest: BottariTemplateCreateRequest): Result<Long>

    suspend fun fetchBottariTemplateDetail(bottariId: Long): Result<BottariTemplateFetchResponse>

    suspend fun takeBottariTemplate(bottariId: Long): Result<Long?>

    suspend fun fetchMyBottariTemplates(): Result<List<BottariTemplateFetchResponse>>

    suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): Result<Unit>
}
