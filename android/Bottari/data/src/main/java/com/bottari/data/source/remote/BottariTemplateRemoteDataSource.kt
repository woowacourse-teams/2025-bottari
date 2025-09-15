package com.bottari.data.source.remote

import com.bottari.data.model.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.bottari.template.BottariTemplateFetchResponse

interface BottariTemplateRemoteDataSource {
    suspend fun fetchBottariTemplates(searchWord: String?): Result<List<BottariTemplateFetchResponse>>

    suspend fun createBottariTemplate(bottariTemplateCreateRequest: BottariTemplateCreateRequest): Result<Long?>

    suspend fun fetchBottariTemplateDetail(bottariId: Long): Result<BottariTemplateFetchResponse>

    suspend fun takeBottariTemplate(bottariId: Long): Result<Long?>

    suspend fun fetchMyBottariTemplates(): Result<List<BottariTemplateFetchResponse>>

    suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): Result<Unit>
}
