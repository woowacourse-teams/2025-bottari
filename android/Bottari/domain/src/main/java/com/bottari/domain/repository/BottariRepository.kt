package com.bottari.domain.repository

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.model.exception.BottariResult

interface BottariRepository {
    suspend fun fetchBottaries(): BottariResult<List<BottariState>>

    suspend fun fetchBottariDetail(id: Long): BottariResult<Bottari>

    suspend fun createBottari(title: String): BottariResult<Long>

    suspend fun deleteBottari(id: Long): BottariResult<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): BottariResult<Unit>
}
