package com.bottari.data.mapper.alarm

import com.bottari.data.model.alarm.AlarmCreateLocationRequest
import com.bottari.data.model.alarm.AlarmCreateRequest
import com.bottari.data.model.alarm.AlarmCreateRoutineRequest
import com.bottari.data.model.alarm.AlarmFetchResponse
import com.bottari.data.model.alarm.AlarmLocationResponse
import com.bottari.data.model.alarm.AlarmRoutineResponse
import com.bottari.data.model.alarm.AlarmSaveLocationRequest
import com.bottari.data.model.alarm.AlarmSaveRequest
import com.bottari.data.model.alarm.AlarmSaveRoutineRequest
import com.bottari.data.model.bottari.BottariAlarmFetchResponse
import com.bottari.data.model.bottari.BottariAlarmLocationResponse
import com.bottari.data.model.bottari.BottariAlarmRoutineResponse
import com.bottari.data.model.bottari.BottariesAlarmFetchResponse
import com.bottari.data.model.bottari.BottariesAlarmLocationResponse
import com.bottari.data.model.bottari.BottariesAlarmRoutineResponse
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

    fun Alarm.toSaveRequest(): AlarmSaveRequest =
        AlarmSaveRequest(
            routineAlarm = toSaveRoutineRequest(),
            locationAlarm = location?.toSaveRequest(),
        )

    fun Alarm.toCreateRequest(): AlarmCreateRequest =
        AlarmCreateRequest(
            routineAlarm = toCreateRoutineRequest(),
            locationAlarm = location?.toCreateRequest(),
        )

    fun AlarmFetchResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    fun BottariesAlarmFetchResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    fun BottariAlarmFetchResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    fun TeamBottariAlarmFetchResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    fun TeamBottariDetailAlarmFetchResponse.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toAlarmType(),
            location = location?.toDomain(),
        )

    fun NotificationEntity.toAlarmDomain(): Alarm =
        Alarm(
            id = alarmId,
            isActive = isActive,
            time = time,
            alarmType = this.toTypeDomain(),
            location = null,
        )

    private fun Alarm.toCreateRoutineRequest(): AlarmCreateRoutineRequest =
        AlarmCreateRoutineRequest(
            time = time,
            type = alarmType.toTypeString(),
            date = alarmType.getDate(),
            daysOfWeek = alarmType.getDaysOfWeek(),
        )

    private fun Alarm.toSaveRoutineRequest(): AlarmSaveRoutineRequest =
        AlarmSaveRoutineRequest(
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

    private fun LocationAlarm.toCreateRequest(): AlarmCreateLocationRequest =
        AlarmCreateLocationRequest(
            isLocationAlarmActive = isActive,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
        )

    private fun LocationAlarm.toSaveRequest(): AlarmSaveLocationRequest =
        AlarmSaveLocationRequest(
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

    private fun BottariesAlarmRoutineResponse.toAlarmType(): AlarmType =
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

    private fun BottariAlarmRoutineResponse.toAlarmType(): AlarmType =
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

    private fun AlarmLocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun BottariesAlarmLocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun BottariAlarmLocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun TeamBottariAlarmLocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun TeamBottariDetailAlarmLocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    fun NotificationEntity.toTypeDomain(): AlarmType =
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
