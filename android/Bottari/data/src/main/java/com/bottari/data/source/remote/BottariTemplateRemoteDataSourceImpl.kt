package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.template.CreateBottariTemplateRequest
import com.bottari.data.model.template.FetchBottariTemplateResponse
import com.bottari.data.model.template.FetchMyBottariTemplatesResponse
import com.bottari.data.service.BottariTemplateService

class BottariTemplateRemoteDataSourceImpl(
    private val bottariTemplateService: BottariTemplateService,
) : BottariTemplateRemoteDataSource {
    override suspend fun fetchBottariTemplates(searchWord: String?): Result<List<FetchBottariTemplateResponse>> =
        safeApiCall { bottariTemplateService.fetchBottariTemplates(searchWord) }

    override suspend fun createBottariTemplate(createBottariTemplateRequest: CreateBottariTemplateRequest): Result<Long?> =
        runCatching {
            val response =
                bottariTemplateService.createBottariTemplate(createBottariTemplateRequest)
            response.extractIdFromHeader(HEADER_TEMPLATE_ID_PREFIX)
        }

    override suspend fun fetchBottariTemplateDetail(bottariId: Long): Result<FetchBottariTemplateResponse> =
        safeApiCall { bottariTemplateService.fetchBottariTemplateDetail(bottariId) }

    override suspend fun takeBottariTemplate(bottariId: Long): Result<Long?> =
        runCatching {
            val response = bottariTemplateService.takeBottariTemplate(bottariId)
            response.extractIdFromHeader(HEADER_BOTTARI_ID_PREFIX)
        }

    override suspend fun fetchMyBottariTemplates(): Result<List<FetchMyBottariTemplatesResponse>> =
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
