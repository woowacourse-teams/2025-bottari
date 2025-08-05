package com.bottari.data.repository

import com.bottari.data.mapper.AlarmMapper.toRequest
import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
) : AlarmRepository {
    override suspend fun saveAlarm(
        ssaid: String,
        id: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmRemoteDataSource.saveAlarm(ssaid, id, alarm.toRequest())

    override suspend fun createAlarm(
        ssaid: String,
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmRemoteDataSource.createAlarm(ssaid, bottariId, alarm.toRequest())

    override suspend fun activeAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit> = alarmRemoteDataSource.activeAlarmState(id = alarmId, ssaid = ssaid)

    override suspend fun inactiveAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit> = alarmRemoteDataSource.inactiveAlarmState(id = alarmId, ssaid = ssaid)
}
