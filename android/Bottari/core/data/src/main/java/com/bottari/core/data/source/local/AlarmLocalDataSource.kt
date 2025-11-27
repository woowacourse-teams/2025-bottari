package com.bottari.core.data.source.local

import com.bottari.core.local.entity.bottari.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface AlarmLocalDataSource {
    fun findAlarm(bottariId: Long): Flow<AlarmEntity?>

    suspend fun saveAlarm(alarm: AlarmEntity): Result<Unit>

    suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    ): Result<Unit>
}
