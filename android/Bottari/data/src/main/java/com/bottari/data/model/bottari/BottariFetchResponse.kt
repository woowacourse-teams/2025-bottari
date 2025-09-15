package com.bottari.data.model.bottari

import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class BottariFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("alarm")
    val alarm: BottariAlarmFetchResponse?,
    @SerialName("items")
    val items: List<BottariItemFetchResponse>,
    @SerialName("title")
    val title: String,
)

@Serializable
data class BottariAlarmFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("location")
    val location: BottariAlarmLocationResponse?,
    @SerialName("routine")
    val routine: BottariAlarmRoutineResponse,
)

@Serializable
data class BottariAlarmLocationResponse(
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("radius")
    val radius: Int,
)

@Serializable
data class BottariAlarmRoutineResponse(
    @SerialName("date")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate?,
    @SerialName("time")
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    @SerialName("dayOfWeeks")
    val dayOfWeeks: List<Int>,
    @SerialName("type")
    val type: String,
)

@Serializable
data class BottariItemFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
