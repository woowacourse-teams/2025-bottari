package com.bottari.data.repository

import com.bottari.data.model.local.bottari.AlarmEntity
import com.bottari.data.source.local.bottari.AlarmLocalDataSource
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmRepositoryImpl(
    private val alarmLocalDataSource: AlarmLocalDataSource,
) : AlarmRepository {
    override fun fetchAlarm(bottariId: Long): Flow<Alarm> =
        alarmLocalDataSource
            .fetchAlarm(bottariId)
            .map { alarm -> alarm.toDomain() }

    override suspend fun saveAlarm(
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmLocalDataSource.saveAlarm(AlarmEntity.fromDomain(bottariId, alarm))

    override suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    ): Result<Unit> = alarmLocalDataSource.updateAlarmActivate(id, isActive)
}
