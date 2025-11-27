package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.bottari.template.BottariTemplateCreateRequest
import com.bottari.core.network.dto.bottari.template.BottariTemplateCursorFetchResponse
import com.bottari.core.network.dto.bottari.template.BottariTemplateFetchResponse
import com.bottari.core.network.dto.common.PageableRequest
import com.bottari.core.network.dto.common.PageableResponse
import com.bottari.core.network.service.BottariTemplateService
import com.bottari.data.common.extension.extractIdFromHeader
import javax.inject.Inject

class BottariTemplateRemoteDataSourceImpl @Inject constructor(
    private val bottariTemplateService: BottariTemplateService,
) : BottariTemplateRemoteDataSource {
    override suspend fun searchTemplatesByTitle(
        title: String,
        pageableRequest: PageableRequest,
    ): Result<PageableResponse<BottariTemplateCursorFetchResponse>> =
        safeApiCall {
            val pageableParams = pageableRequest.toQueryMap().toMutableMap()
            pageableParams["query"] = title
            bottariTemplateService.searchTemplatesByTitle(pageableParams)
        }

    override suspend fun searchTemplatesByHashtag(
        hashtagId: Long,
        pageableRequest: PageableRequest,
    ): Result<PageableResponse<BottariTemplateCursorFetchResponse>> =
        safeApiCall {
            val pageableParams = pageableRequest.toQueryMap().toMutableMap()
            pageableParams["hashtagId"] = hashtagId.toString()
            bottariTemplateService.searchTemplatesByHashtag(pageableParams)
        }

    override suspend fun createBottariTemplate(bottariTemplateCreateRequest: BottariTemplateCreateRequest): Result<Long> =
        runCatching {
            bottariTemplateService
                .createBottariTemplate(bottariTemplateCreateRequest)
                .let { response ->
                    response.extractIdFromHeader(HEADER_TEMPLATE_ID_PREFIX)
                        ?: throw IllegalStateException("응답 헤더에서 템플릿 ID를 찾을 수 없습니다.")
                }
        }

    override suspend fun fetchBottariTemplateDetail(bottariId: Long): Result<BottariTemplateFetchResponse> =
        safeApiCall { bottariTemplateService.fetchBottariTemplateDetail(bottariId) }

    override suspend fun takeBottariTemplate(bottariId: Long): Result<Long?> =
        runCatching {
            val response = bottariTemplateService.takeBottariTemplate(bottariId)
            response.extractIdFromHeader(HEADER_BOTTARI_ID_PREFIX)
        }

    override suspend fun fetchMyBottariTemplates(): Result<List<BottariTemplateFetchResponse>> =
        safeApiCall {
            bottariTemplateService.fetchMyBottariTemplates()
        }

    override suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): Result<Unit> =
        safeApiCall {
            bottariTemplateService.deleteMyBottariTemplate(bottariTemplateId)
        }

    companion object {
        private const val HEADER_TEMPLATE_ID_PREFIX = "/templates/"
        private const val HEADER_BOTTARI_ID_PREFIX = "/bottaries/"
    }
}
