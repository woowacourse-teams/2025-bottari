package com.bottari.data.source.remote

import com.bottari.data.model.bottari.BottariResponse

interface BottariDetailRemoteDataSource {
    suspend fun findBottari(
        id: Long,
        ssaid: String,
    ): Result<BottariResponse>
}
