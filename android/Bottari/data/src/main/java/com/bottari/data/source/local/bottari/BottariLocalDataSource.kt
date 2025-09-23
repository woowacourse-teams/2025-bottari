package com.bottari.data.source.local.bottari

import com.bottari.data.model.local.bottari.BottariEntity
import kotlinx.coroutines.flow.Flow

interface BottariLocalDataSource {
    fun fetchBottaries(): Flow<List<BottariEntity>>

    suspend fun saveBottari(bottari: BottariEntity): Result<Unit>

    suspend fun deleteBottari(bottariId: Long): Result<Unit>

    suspend fun updateBottariTitle(
        bottariId: Long,
        title: String,
    ): Result<Unit>
}
