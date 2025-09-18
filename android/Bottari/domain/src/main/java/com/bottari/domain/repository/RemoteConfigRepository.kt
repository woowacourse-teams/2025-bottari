package com.bottari.domain.repository

import com.bottari.domain.model.exception.BottariResult

interface RemoteConfigRepository {
    suspend fun getMinUpdateVersionCode(): BottariResult<Int>
}
