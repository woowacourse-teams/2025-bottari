package com.bottari.data.model.alarm

import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.LocationAlarm
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class AlarmCreateRequest(
    @SerialName("routineAlarm")
    val routineAlarm: AlarmRoutineCreateRequest,
    @SerialName("locationAlarm")
    val locationAlarm: AlarmLocationCreateRequest?,
) {
    companion object {
        fun fromDomain(alarm: Alarm): AlarmCreateRequest =
            AlarmCreateRequest(
                routineAlarm = AlarmRoutineCreateRequest.fromDomain(alarm),
                locationAlarm = alarm.location?.let { AlarmLocationCreateRequest.fromDomain(it) },
            )
    }
}

@Serializable
data class AlarmLocationCreateRequest(
    @SerialName("isLocationAlarmActive")
    val isLocationAlarmActive: Boolean,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("radius")
    val radius: Int,
) {
    companion object {
        fun fromDomain(locationAlarm: LocationAlarm): AlarmLocationCreateRequest =
            AlarmLocationCreateRequest(
                isLocationAlarmActive = locationAlarm.isActive,
                latitude = locationAlarm.latitude,
                longitude = locationAlarm.longitude,
                radius = locationAlarm.radius,
            )
    }
}

@Serializable
data class AlarmRoutineCreateRequest(
    @SerialName("time")
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    @SerialName("type")
    val type: String,
    @SerialName("date")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate?,
    @SerialName("repeatDayOfWeekValues")
    val daysOfWeek: List<Int>,
) {
    companion object {
        fun fromDomain(alarm: Alarm): AlarmRoutineCreateRequest =
            AlarmRoutineCreateRequest(
                time = alarm.time,
                type = alarm.alarmType.toTypeString(),
                date = alarm.alarmType.getAlarmDate(),
                daysOfWeek = alarm.alarmType.getDaysOfWeek(),
            )
    }
}
