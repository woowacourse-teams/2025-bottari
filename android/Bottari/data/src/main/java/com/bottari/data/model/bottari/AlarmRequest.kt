package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlarmRequest(
    @SerialName("routineAlarm")
    val routineAlarm: RoutineRequest,
    @SerialName("locationAlarm")
    val locationAlarm: LocationRequest?,
)
