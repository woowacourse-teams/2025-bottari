package com.bottari.domain.repository

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.exception.BottariResult

interface AlarmRepository {
    suspend fun saveAlarm(
        id: Long,
        alarm: Alarm,
    ): BottariResult<Unit>

    suspend fun createAlarm(
        bottariId: Long,
        alarm: Alarm,
    ): BottariResult<Long>

    suspend fun activeAlarm(alarmId: Long): BottariResult<Unit>

    suspend fun inactiveAlarm(alarmId: Long): BottariResult<Unit>
}
