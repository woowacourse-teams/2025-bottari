package com.bottari.data.source.remote

import com.bottari.data.model.template.FetchBottariTemplateResponse

interface BottariTemplateRemoteDataSource {
    suspend fun fetchBottariTemplates(searchWord: String?): Result<List<FetchBottariTemplateResponse>>

    suspend fun fetchBottariTemplateDetail(bottariId: Long): Result<FetchBottariTemplateResponse>

    suspend fun takeBottariTemplate(
        ssaid: String,
        bottariId: Long,
    ): Result<Long?>
}
