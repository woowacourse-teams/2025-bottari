package com.bottari.data.model.alarm

import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class AlarmSaveRequest(
    @SerialName("routineAlarm")
    val routineAlarm: AlarmRoutineSaveRequest,
    @SerialName("locationAlarm")
    val locationAlarm: AlarmLocationSaveRequest?,
)

@Serializable
data class AlarmLocationSaveRequest(
    @SerialName("isLocationAlarmActive")
    val isLocationAlarmActive: Boolean,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("radius")
    val radius: Int,
)

@Serializable
data class AlarmRoutineSaveRequest(
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
)
