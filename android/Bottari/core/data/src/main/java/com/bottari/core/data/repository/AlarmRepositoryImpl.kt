package com.bottari.core.data.repository

import com.bottari.core.data.source.local.AlarmLocalDataSource
import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.repository.AlarmRepository
import com.bottari.core.local.entity.bottari.AlarmEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmLocalDataSource: AlarmLocalDataSource,
) : AlarmRepository {
    override fun findAlarm(bottariId: Long): Flow<Alarm?> =
        alarmLocalDataSource
            .findAlarm(bottariId)
            .map { alarm -> alarm?.toDomain() }

    override suspend fun saveAlarm(
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmLocalDataSource.saveAlarm(AlarmEntity.fromDomain(bottariId, alarm))

    override suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    ): Result<Unit> = alarmLocalDataSource.updateAlarmActivate(id, isActive)
}
