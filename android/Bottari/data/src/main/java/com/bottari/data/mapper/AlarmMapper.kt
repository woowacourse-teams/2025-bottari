package com.bottari.data.mapper

import com.bottari.data.model.bottari.AlarmResponse
import com.bottari.data.model.bottari.LocationResponse
import com.bottari.data.model.bottari.RoutineResponse
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.alarm.LocationAlarm
import java.time.LocalTime

object AlarmMapper {
    fun AlarmResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.toLocalTime(),
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    private fun RoutineResponse.toAlarmType(): AlarmType =
        when (type.uppercase()) {
            "NON_REPEAT" ->
                AlarmType.NonRepeat(
                    date = date ?: throw IllegalArgumentException("날짜가 존재하지 않습니다"),
                )

            "EVERY_DAY_REPEAT" -> AlarmType.EveryDayRepeat
            "EVERY_WEEK_REPEAT" ->
                AlarmType.EveryWeekRepeat(
                    daysOfWeek = dayOfWeeks,
                )

            else -> throw IllegalArgumentException("Unknown alarm type: $type")
        }

    private fun RoutineResponse.toLocalTime(): LocalTime = time

    private fun LocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )
}
