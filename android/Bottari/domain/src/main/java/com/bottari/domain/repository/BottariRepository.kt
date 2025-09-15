package com.bottari.domain.repository

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState

interface BottariRepository {
    suspend fun fetchBottaries(): Result<List<BottariState>>

    suspend fun fetchBottariDetail(id: Long): Result<Bottari>

    suspend fun createBottari(title: String): Result<Long?>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): Result<Unit>
}
