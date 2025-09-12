package com.bottari.data.mapper

import com.bottari.data.model.alarm.AlarmCreateRequest
import com.bottari.data.model.alarm.AlarmFetchResponse
import com.bottari.data.model.alarm.AlarmLocationRequest
import com.bottari.data.model.alarm.AlarmLocationResponse
import com.bottari.data.model.alarm.AlarmRoutineRequest
import com.bottari.data.model.alarm.AlarmRoutineResponse
import com.bottari.data.model.alarm.AlarmSaveRequest
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
    private const val DAYS_IN_WEEK = 7

    fun Alarm.toSaveRequest(): AlarmSaveRequest =
        AlarmSaveRequest(
            routineAlarm = toRoutineRequest(),
            locationAlarm = location?.toRequest(),
        )

    fun Alarm.toCreateRequest(): AlarmCreateRequest =
        AlarmCreateRequest(
            routineAlarm = toRoutineRequest(),
            locationAlarm = location?.toRequest(),
        )

    fun AlarmFetchResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    private fun Alarm.toRoutineRequest(): AlarmRoutineRequest =
        AlarmRoutineRequest(
            time = time,
            type = alarmType.toTypeString(),
            date = alarmType.getDate(),
            daysOfWeek = alarmType.getDaysOfWeek(),
        )

    private fun AlarmType.toTypeString(): String =
        when (this) {
            is AlarmType.NonRepeat -> NON_REPEAT
            is AlarmType.Repeat -> if (repeatDays.size == DAYS_IN_WEEK) EVERY_DAY_REPEAT else EVERY_WEEK_REPEAT
        }

    private fun AlarmType.getDate(): LocalDate? {
        if (this !is AlarmType.NonRepeat) return null
        return date
    }

    private fun AlarmType.getDaysOfWeek(): List<Int> {
        if (this !is AlarmType.Repeat) return emptyList()
        return repeatDays
    }

    private fun LocationAlarm.toRequest(): AlarmLocationRequest =
        AlarmLocationRequest(
            isLocationAlarmActive = isActive,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
        )

    private fun AlarmRoutineResponse.toAlarmType(): AlarmType =
        when (type.uppercase()) {
            NON_REPEAT ->
                AlarmType.NonRepeat(
                    date = date ?: throw IllegalArgumentException(ERROR_MISSING_DATE),
                )

            EVERY_DAY_REPEAT,
            EVERY_WEEK_REPEAT,
            -> AlarmType.Repeat(dayOfWeeks)

            else -> throw IllegalArgumentException(ERROR_UNKNOWN_ALARM_TYPE.format(type))
        }

    private fun AlarmLocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )
}
