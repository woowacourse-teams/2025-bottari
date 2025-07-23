package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.FindBottariRequest

interface BottariDetailRemoteDataSource {
    suspend fun findBottari(request: FindBottariRequest): Result<BottariResponse>
}
