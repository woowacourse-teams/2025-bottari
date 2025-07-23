package com.bottari.data.source.remote

import com.bottari.data.model.template.FetchBottariTemplateResponse

interface BottariTemplateRemoteDataSource {
    suspend fun fetchBottariTemplates(): Result<List<FetchBottariTemplateResponse>>
}
