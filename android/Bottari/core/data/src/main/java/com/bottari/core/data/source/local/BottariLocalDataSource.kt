package com.bottari.core.data.source.local

import com.bottari.core.local.entity.bottari.BottariEntity
import com.bottari.core.local.entity.bottari.BottariWithAlarm
import com.bottari.core.local.entity.bottari.BottariWithAlarmAndItems
import kotlinx.coroutines.flow.Flow

interface BottariLocalDataSource {
    fun fetchBottaries(): Flow<List<BottariWithAlarmAndItems>>

    suspend fun fetchBottariesWithAlarm(): Result<List<BottariWithAlarm>>

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
