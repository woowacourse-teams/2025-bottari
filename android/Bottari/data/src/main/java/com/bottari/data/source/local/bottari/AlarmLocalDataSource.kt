package com.bottari.data.source.local.bottari

import com.bottari.data.model.local.bottari.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface AlarmLocalDataSource {
    fun fetchAlarm(bottariId: Long): Result<Flow<AlarmEntity>>

    suspend fun saveAlarm(alarm: AlarmEntity): Result<Unit>

    suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    ): Result<Unit>
}
