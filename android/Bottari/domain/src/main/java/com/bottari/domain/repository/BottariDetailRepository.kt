package com.bottari.domain.repository

import com.bottari.domain.model.bottari.BottariDetail

interface BottariDetailRepository {
    suspend fun findBottariDetail(
        id: Long,
        ssaid: String,
    ): Result<BottariDetail>
}
