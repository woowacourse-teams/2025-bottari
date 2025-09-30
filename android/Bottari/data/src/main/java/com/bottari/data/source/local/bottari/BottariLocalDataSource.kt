package com.bottari.data.source.local.bottari

import com.bottari.data.model.local.bottari.BottariEntity
import com.bottari.data.model.local.bottari.BottariWithAlarmAndItems
import kotlinx.coroutines.flow.Flow

interface BottariLocalDataSource {
    fun fetchBottaries(): Flow<List<BottariWithAlarmAndItems>>

    fun findBottari(id: Long): Flow<BottariWithAlarmAndItems?>

    suspend fun createBottari(bottari: BottariEntity): Result<Long>

    suspend fun createBottariWithItems(
        bottari: BottariEntity,
        itemNames: List<String>,
    ): Result<Long>

    suspend fun deleteBottari(bottariId: Long): Result<Unit>

    suspend fun updateBottariTitle(
        bottariId: Long,
        title: String,
    ): Result<Unit>
}
