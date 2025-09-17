package com.bottari.data.source.remote

import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.remote.bottari.template.BottariTemplateFetchResponse
import com.bottari.domain.model.exception.BottariResult

interface BottariTemplateRemoteDataSource {
    suspend fun fetchBottariTemplates(searchWord: String?): BottariResult<List<BottariTemplateFetchResponse>>

    suspend fun createBottariTemplate(bottariTemplateCreateRequest: BottariTemplateCreateRequest): BottariResult<Long>

    suspend fun fetchBottariTemplateDetail(bottariId: Long): BottariResult<BottariTemplateFetchResponse>

    suspend fun takeBottariTemplate(bottariId: Long): BottariResult<Long>

    suspend fun fetchMyBottariTemplates(): BottariResult<List<BottariTemplateFetchResponse>>

    suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): BottariResult<Unit>
}
