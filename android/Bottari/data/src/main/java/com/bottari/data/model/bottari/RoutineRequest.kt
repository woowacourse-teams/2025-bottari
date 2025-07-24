package com.bottari.data.model.bottari

import com.bottari.data.util.LocalDateSerializer
import com.bottari.data.util.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class RoutineRequest(
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
