package com.bottari.data.source.local.bottari

import com.bottari.data.local.bottari.AlarmDao
import com.bottari.data.model.local.bottari.AlarmEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmLocalDataSourceImpl @Inject constructor(
    private val dao: AlarmDao,
) : AlarmLocalDataSource {
    override fun findAlarm(bottariId: Long): Flow<AlarmEntity?> = dao.findAlarm(bottariId)

    override suspend fun saveAlarm(alarm: AlarmEntity): Result<Unit> = runCatching { dao.saveAlarm(alarm) }

    override suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    ): Result<Unit> = runCatching { dao.updateAlarmActivate(id, isActive) }
}
