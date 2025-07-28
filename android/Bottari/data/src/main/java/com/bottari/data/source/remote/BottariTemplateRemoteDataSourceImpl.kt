package com.bottari.data.source.remote

import com.bottari.data.extension.extractIdFromHeader
import com.bottari.data.model.template.CreateBottariTemplateRequest
import com.bottari.data.model.template.FetchBottariTemplateResponse
import com.bottari.data.model.template.FetchMyBottariTemplatesResponse
import com.bottari.data.service.BottariTemplateService
import com.bottari.data.util.safeApiCall

class BottariTemplateRemoteDataSourceImpl(
    private val bottariTemplateService: BottariTemplateService,
) : BottariTemplateRemoteDataSource {
    override suspend fun fetchBottariTemplates(searchWord: String?): Result<List<FetchBottariTemplateResponse>> =
        safeApiCall { bottariTemplateService.fetchBottariTemplates(searchWord) }

    override suspend fun createBottariTemplate(
        ssaid: String,
        createBottariTemplateRequest: CreateBottariTemplateRequest,
    ): Result<Long?> =
        runCatching {
            val response =
                bottariTemplateService.createBottariTemplate(ssaid, createBottariTemplateRequest)
            response.extractIdFromHeader(HEADER_TEMPLATE_ID_PREFIX)
        }

    override suspend fun fetchBottariTemplateDetail(bottariId: Long): Result<FetchBottariTemplateResponse> =
        safeApiCall { bottariTemplateService.fetchBottariTemplateDetail(bottariId) }

    override suspend fun takeBottariTemplate(
        ssaid: String,
        bottariId: Long,
    ): Result<Long?> =
        runCatching {
            val response = bottariTemplateService.takeBottariTemplate(ssaid, bottariId)
            response.extractIdFromHeader(HEADER_BOTTARI_ID_PREFIX)
        }

    override suspend fun fetchMyBottariTemplates(ssaid: String): Result<List<FetchMyBottariTemplatesResponse>> =
        safeApiCall {
            bottariTemplateService.fetchMyBottariTemplates(ssaid)
        }

    companion object {
        private const val HEADER_TEMPLATE_ID_PREFIX = "/templates/"
        private const val HEADER_BOTTARI_ID_PREFIX = "/bottaries/"
    }
}
