package com.bottari.core.data.source.local

import com.bottari.core.local.database.bottari.AlarmDao
import com.bottari.core.local.entity.bottari.AlarmEntity
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
