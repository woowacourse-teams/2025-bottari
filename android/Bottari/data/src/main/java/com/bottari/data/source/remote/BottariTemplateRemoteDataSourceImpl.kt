package com.bottari.data.source.remote

import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.remote.bottari.template.BottariTemplateFetchResponse
import com.bottari.data.service.BottariTemplateService
import com.bottari.domain.model.exception.BottariResult

class BottariTemplateRemoteDataSourceImpl(
    private val bottariTemplateService: BottariTemplateService,
) : BottariTemplateRemoteDataSource {
    override suspend fun fetchBottariTemplates(searchWord: String?): BottariResult<List<BottariTemplateFetchResponse>> =
        bottariTemplateService.fetchBottariTemplates(searchWord)

    override suspend fun createBottariTemplate(bottariTemplateCreateRequest: BottariTemplateCreateRequest): BottariResult<Long> =
        bottariTemplateService.createBottariTemplate(bottariTemplateCreateRequest)

    override suspend fun fetchBottariTemplateDetail(bottariId: Long): BottariResult<BottariTemplateFetchResponse> =
        bottariTemplateService.fetchBottariTemplateDetail(bottariId)

    override suspend fun takeBottariTemplate(bottariId: Long): BottariResult<Long> = bottariTemplateService.takeBottariTemplate(bottariId)

    override suspend fun fetchMyBottariTemplates(): BottariResult<List<BottariTemplateFetchResponse>> =
        bottariTemplateService.fetchMyBottariTemplates()

    override suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): BottariResult<Unit> =
        bottariTemplateService.deleteMyBottariTemplate(bottariTemplateId)
}
