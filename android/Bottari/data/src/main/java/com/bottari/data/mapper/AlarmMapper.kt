package com.bottari.data.mapper

import com.bottari.data.model.bottari.AlarmRequest
import com.bottari.data.model.bottari.AlarmResponse
import com.bottari.data.model.bottari.LocationRequest
import com.bottari.data.model.bottari.LocationResponse
import com.bottari.data.model.bottari.RoutineRequest
import com.bottari.data.model.bottari.RoutineResponse
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.alarm.LocationAlarm
import java.time.LocalDate

object AlarmMapper {
    private const val ERROR_MISSING_DATE = "NON_REPEAT 유형의 알람에는 날짜 정보가 필요합니다."
    private const val ERROR_UNKNOWN_ALARM_TYPE = "지원하지 않는 알람 유형입니다: %s"
    private const val NON_REPEAT = "NON_REPEAT"
    private const val EVERY_DAY_REPEAT = "EVERY_DAY_REPEAT"
    private const val EVERY_WEEK_REPEAT = "EVERY_WEEK_REPEAT"

    fun Alarm.toRequest(): AlarmRequest =
        AlarmRequest(
            routineAlarm = toRoutineRequest(),
            locationAlarm = location?.toRequest(),
        )

    fun AlarmResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    private fun Alarm.toRoutineRequest(): RoutineRequest =
        RoutineRequest(
            time = time,
            type = alarmType.toTypeString(),
            date = alarmType.getDate(),
            daysOfWeek = alarmType.getDaysOfWeek(),
        )

    private fun AlarmType.toTypeString(): String =
        when (this) {
            is AlarmType.NonRepeat -> NON_REPEAT
            is AlarmType.EveryDayRepeat -> EVERY_DAY_REPEAT
            is AlarmType.EveryWeekRepeat -> EVERY_WEEK_REPEAT
        }

    private fun AlarmType.getDate(): LocalDate? {
        if (this !is AlarmType.NonRepeat) return null
        return date
    }

    private fun AlarmType.getDaysOfWeek(): List<Int> {
        if (this !is AlarmType.EveryWeekRepeat) return emptyList()
        return daysOfWeek
    }

    private fun LocationAlarm.toRequest(): LocationRequest =
        LocationRequest(
            isLocationAlarmActive = isActive,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
        )

    private fun RoutineResponse.toAlarmType(): AlarmType =
        when (type.uppercase()) {
            NON_REPEAT ->
                AlarmType.NonRepeat(
                    date = date ?: throw IllegalArgumentException(ERROR_MISSING_DATE),
                )

            EVERY_DAY_REPEAT -> AlarmType.EveryDayRepeat

            EVERY_WEEK_REPEAT ->
                AlarmType.EveryWeekRepeat(
                    daysOfWeek = dayOfWeeks,
                )

            else -> throw IllegalArgumentException(ERROR_UNKNOWN_ALARM_TYPE.format(type))
        }

    private fun LocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )
}
