package com.bottari.data.source.remote

import com.bottari.data.model.template.CreateBottariTemplateRequest
import com.bottari.data.model.template.FetchBottariTemplateResponse
import com.bottari.data.model.template.FetchMyBottariTemplatesResponse

interface BottariTemplateRemoteDataSource {
    suspend fun fetchBottariTemplates(searchWord: String?): Result<List<FetchBottariTemplateResponse>>

    suspend fun createBottariTemplate(createBottariTemplateRequest: CreateBottariTemplateRequest): Result<Long?>

    suspend fun fetchBottariTemplateDetail(bottariId: Long): Result<FetchBottariTemplateResponse>

    suspend fun takeBottariTemplate(bottariId: Long): Result<Long?>

    suspend fun fetchMyBottariTemplates(): Result<List<FetchMyBottariTemplatesResponse>>

    suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): Result<Unit>
}
