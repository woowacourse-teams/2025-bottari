package com.bottari.data.source.local.bottari

import com.bottari.data.local.bottari.AlarmDao
import com.bottari.data.model.local.bottari.AlarmEntity
import kotlinx.coroutines.flow.Flow

class AlarmLocalDataSourceImpl(
    private val dao: AlarmDao,
) : AlarmLocalDataSource {
    override fun fetchAlarm(bottariId: Long): Flow<AlarmEntity> = dao.fetchAlarm(bottariId)

    override suspend fun saveAlarm(alarm: AlarmEntity): Result<Unit> = runCatching { dao.saveAlarm(alarm) }

    override suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    ): Result<Unit> = runCatching { dao.updateAlarmActivate(id, isActive) }
}
