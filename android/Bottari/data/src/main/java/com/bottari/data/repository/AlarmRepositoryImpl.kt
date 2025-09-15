package com.bottari.data.repository

import com.bottari.data.mapper.alarm.AlarmMapper.toCreateRequest
import com.bottari.data.mapper.alarm.AlarmMapper.toSaveRequest
import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
) : AlarmRepository {
    override suspend fun saveAlarm(
        id: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmRemoteDataSource.saveAlarm(id, alarm.toSaveRequest())

    override suspend fun createAlarm(
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmRemoteDataSource.createAlarm(bottariId, alarm.toCreateRequest())

    override suspend fun activeAlarm(alarmId: Long): Result<Unit> = alarmRemoteDataSource.activeAlarmState(id = alarmId)

    override suspend fun inactiveAlarm(alarmId: Long): Result<Unit> = alarmRemoteDataSource.inactiveAlarmState(id = alarmId)
}
