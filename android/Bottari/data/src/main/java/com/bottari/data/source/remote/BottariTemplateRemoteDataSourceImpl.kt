package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.remote.bottari.template.BottariTemplateCursorFetchResponse
import com.bottari.data.model.remote.bottari.template.BottariTemplateFetchResponse
import com.bottari.data.model.remote.common.PageableRequest
import com.bottari.data.model.remote.common.PageableResponse
import com.bottari.data.service.BottariTemplateService
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
            val response = bottariTemplateService.createBottariTemplate(bottariTemplateCreateRequest)
            response.extractIdFromHeader(HEADER_TEMPLATE_ID_PREFIX) ?: -1
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
