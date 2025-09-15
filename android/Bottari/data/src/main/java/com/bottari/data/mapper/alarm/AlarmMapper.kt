package com.bottari.data.mapper.alarm

import com.bottari.data.model.alarm.AlarmCreateRequest
import com.bottari.data.model.alarm.AlarmFetchResponse
import com.bottari.data.model.alarm.AlarmLocationCreateRequest
import com.bottari.data.model.alarm.AlarmLocationFetchResponse
import com.bottari.data.model.alarm.AlarmLocationSaveRequest
import com.bottari.data.model.alarm.AlarmRoutineCreateRequest
import com.bottari.data.model.alarm.AlarmRoutineFetchResponse
import com.bottari.data.model.alarm.AlarmRoutineSaveRequest
import com.bottari.data.model.alarm.AlarmSaveRequest
import com.bottari.data.model.bottari.BottariAlarmFetchResponse
import com.bottari.data.model.bottari.BottariAlarmLocationFetchResponse
import com.bottari.data.model.bottari.BottariAlarmRoutineFetchResponse
import com.bottari.data.model.bottari.BottariesAlarmFetchResponse
import com.bottari.data.model.bottari.BottariesAlarmLocationFetchResponse
import com.bottari.data.model.bottari.BottariesAlarmRoutineFetchResponse
import com.bottari.data.model.notification.NotificationEntity
import com.bottari.data.model.team.bottari.TeamBottariAlarmFetchResponse
import com.bottari.data.model.team.bottari.TeamBottariAlarmLocationResponse
import com.bottari.data.model.team.bottari.TeamBottariAlarmRoutineResponse
import com.bottari.data.model.team.bottari.TeamBottariDetailAlarmFetchResponse
import com.bottari.data.model.team.bottari.TeamBottariDetailAlarmLocationResponse
import com.bottari.data.model.team.bottari.TeamBottariDetailAlarmRoutineResponse
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.alarm.LocationAlarm
import java.time.LocalDate

object AlarmMapper {
    private const val REPEAT = "REPEAT"
    private const val ERROR_MISSING_DATE = "NON_REPEAT 유형의 알람에는 날짜 정보가 필요합니다."
    private const val ERROR_UNKNOWN_ALARM_TYPE = "지원하지 않는 알람 유형입니다: %s"
    private const val NON_REPEAT = "NON_REPEAT"
    private const val EVERY_DAY_REPEAT = "EVERY_DAY_REPEAT"
    private const val EVERY_WEEK_REPEAT = "EVERY_WEEK_REPEAT"
    private const val DAYS_IN_WEEK = 7

    fun Alarm.toAlarmSaveRequest(): AlarmSaveRequest =
        AlarmSaveRequest(
            routineAlarm = toSaveRoutineRequest(),
            locationAlarm = location?.toAlarmSaveRequest(),
        )

    fun Alarm.toAlarmCreateRequest(): AlarmCreateRequest =
        AlarmCreateRequest(
            routineAlarm = toAlarmCreateRoutineRequest(),
            locationAlarm = location?.toAlarmCreateRequest(),
        )

    fun AlarmFetchResponse.toAlarm(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toLocationAlarm(),
        )

    fun BottariesAlarmFetchResponse.toAlarm(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toLocationAlarm(),
        )

    fun BottariAlarmFetchResponse.toAlarm(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toLocationAlarm(),
        )

    fun TeamBottariAlarmFetchResponse.toAlarm(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toLocationAlarm(),
        )

    fun TeamBottariDetailAlarmFetchResponse.toAlarm(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toLocationAlarm(),
        )

    fun NotificationEntity.toAlarm(): Alarm =
        Alarm(
            id = alarmId,
            isActive = isActive,
            time = time,
            alarmType = this.toAlarmType(),
            location = null,
        )

    private fun Alarm.toAlarmCreateRoutineRequest(): AlarmRoutineCreateRequest =
        AlarmRoutineCreateRequest(
            time = time,
            type = alarmType.toTypeString(),
            date = alarmType.getDate(),
            daysOfWeek = alarmType.getDaysOfWeek(),
        )

    private fun Alarm.toSaveRoutineRequest(): AlarmRoutineSaveRequest =
        AlarmRoutineSaveRequest(
            time = time,
            type = alarmType.toTypeString(),
            date = alarmType.getDate(),
            daysOfWeek = alarmType.getDaysOfWeek(),
        )

    fun AlarmType.toTypeString(): String =
        when (this) {
            is AlarmType.NonRepeat -> NON_REPEAT
            is AlarmType.Repeat -> if (repeatDays.size == DAYS_IN_WEEK) EVERY_DAY_REPEAT else EVERY_WEEK_REPEAT
        }

    fun AlarmType.getDate(): LocalDate? {
        if (this !is AlarmType.NonRepeat) return null
        return date
    }

    private fun AlarmType.getDaysOfWeek(): List<Int> {
        if (this !is AlarmType.Repeat) return emptyList()
        return repeatDays
    }

    private fun LocationAlarm.toAlarmCreateRequest(): AlarmLocationCreateRequest =
        AlarmLocationCreateRequest(
            isLocationAlarmActive = isActive,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
        )

    private fun LocationAlarm.toAlarmSaveRequest(): AlarmLocationSaveRequest =
        AlarmLocationSaveRequest(
            isLocationAlarmActive = isActive,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
        )

    private fun AlarmRoutineFetchResponse.toAlarmType(): AlarmType =
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

    private fun BottariesAlarmRoutineFetchResponse.toAlarmType(): AlarmType =
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

    private fun BottariAlarmRoutineFetchResponse.toAlarmType(): AlarmType =
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

    private fun TeamBottariAlarmRoutineResponse.toAlarmType(): AlarmType =
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

    private fun TeamBottariDetailAlarmRoutineResponse.toAlarmType(): AlarmType =
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

    private fun AlarmLocationFetchResponse.toLocationAlarm(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun BottariesAlarmLocationFetchResponse.toLocationAlarm(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun BottariAlarmLocationFetchResponse.toLocationAlarm(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun TeamBottariAlarmLocationResponse.toLocationAlarm(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun TeamBottariDetailAlarmLocationResponse.toLocationAlarm(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    fun NotificationEntity.toAlarmType(): AlarmType =
        when (this.alarmType) {
            NON_REPEAT -> AlarmType.NonRepeat(date!!)
            REPEAT -> AlarmType.Repeat(repeatDays)
            else -> throw IllegalArgumentException(ERROR_UNKNOWN_ALARM_TYPE)
        }

    fun AlarmType.getRepeatDays(): List<Int> =
        when (this) {
            is AlarmType.NonRepeat -> emptyList()
            is AlarmType.Repeat -> repeatDays
        }
}
