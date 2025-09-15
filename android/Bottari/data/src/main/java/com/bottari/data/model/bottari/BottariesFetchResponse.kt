package com.bottari.data.model.bottari

import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class BottariesFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: BottariesAlarmFetchResponse?,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
)

@Serializable
data class BottariesAlarmFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("location")
    val location: BottariesAlarmLocationFetchResponse?,
    @SerialName("routine")
    val routine: BottariesAlarmRoutineFetchResponse,
)

@Serializable
data class BottariesAlarmLocationFetchResponse(
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
data class BottariesAlarmRoutineFetchResponse(
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
