package com.bottari.data.mapper

import com.bottari.data.model.bottari.AlarmResponse
import com.bottari.data.model.bottari.LocationResponse
import com.bottari.data.model.bottari.RoutineResponse
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.alarm.LocationAlarm
import java.time.LocalTime

object AlarmMapper {

    private const val ERROR_MISSING_DATE = "NON_REPEAT 유형의 알람에는 날짜 정보가 필요합니다."
    private const val ERROR_UNKNOWN_ALARM_TYPE = "지원하지 않는 알람 유형입니다: %s"

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
            "NON_REPEAT" -> AlarmType.NonRepeat(
                date = date ?: throw IllegalArgumentException(ERROR_MISSING_DATE)
            )

            "EVERY_DAY_REPEAT" -> AlarmType.EveryDayRepeat

            "EVERY_WEEK_REPEAT" -> AlarmType.EveryWeekRepeat(
                daysOfWeek = dayOfWeeks
            )

            else -> throw IllegalArgumentException(ERROR_UNKNOWN_ALARM_TYPE.format(type))
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
