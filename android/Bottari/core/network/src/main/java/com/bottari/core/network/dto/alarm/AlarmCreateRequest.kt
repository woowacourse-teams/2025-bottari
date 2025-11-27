package com.bottari.core.network.dto.alarm

import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.model.alarm.LocationAlarm
import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class AlarmCreateRequest(
    @SerialName("routineAlarm")
    val routineAlarm: AlarmRoutine,
    @SerialName("locationAlarm")
    val locationAlarm: AlarmLocation?,
) {
    companion object {
        fun fromDomain(alarm: Alarm): AlarmCreateRequest =
            AlarmCreateRequest(
                routineAlarm = AlarmRoutine.fromDomain(alarm),
                locationAlarm = alarm.location?.let { AlarmLocation.fromDomain(it) },
            )
    }

    @Serializable
    data class AlarmRoutine(
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
            fun fromDomain(alarm: Alarm): AlarmRoutine =
                AlarmRoutine(
                    time = alarm.time,
                    type = alarm.alarmType.toTypeString(),
                    date = alarm.alarmType.getAlarmDate(),
                    daysOfWeek = alarm.alarmType.getDaysOfWeek(),
                )
        }
    }

    @Serializable
    data class AlarmLocation(
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
            fun fromDomain(locationAlarm: LocationAlarm): AlarmLocation =
                AlarmLocation(
                    isLocationAlarmActive = locationAlarm.isActive,
                    latitude = locationAlarm.latitude,
                    longitude = locationAlarm.longitude,
                    radius = locationAlarm.radius,
                )
        }
    }
}
