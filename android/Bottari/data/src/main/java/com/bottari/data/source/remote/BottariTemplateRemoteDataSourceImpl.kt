package com.bottari.data.source.remote

import com.bottari.data.model.template.FetchBottariTemplateResponse
import com.bottari.data.service.BottariTemplateService
import com.bottari.data.util.safeApiCall

class BottariTemplateRemoteDataSourceImpl(
    private val bottariTemplateService: BottariTemplateService,
) : BottariTemplateRemoteDataSource {
    override suspend fun fetchBottariTemplates(searchWord: String?): Result<List<FetchBottariTemplateResponse>> =
        safeApiCall { bottariTemplateService.fetchBottariTemplates(searchWord) }
}
