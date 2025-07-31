package com.bottari.domain.repository

import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail

interface BottariRepository {
    suspend fun fetchBottaries(ssaid: String): Result<List<Bottari>>

    suspend fun fetchBottariDetail(
        id: Long,
        ssaid: String,
    ): Result<BottariDetail>

    suspend fun createBottari(
        ssaid: String,
        title: String,
    ): Result<Long?>

    suspend fun deleteBottari(
        id: Long,
        ssaid: String,
    ): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        ssaid: String,
        title: String,
    ): Result<Unit>
}
