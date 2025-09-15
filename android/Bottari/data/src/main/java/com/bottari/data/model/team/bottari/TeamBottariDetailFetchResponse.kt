package com.bottari.data.model.team.bottari

import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class TeamBottariDetailFetchResponse(
    @SerialName("id")
    val bottariId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: TeamBottariDetailAlarmFetchResponse?,
    @SerialName("sharedItems")
    val sharedItems: List<TeamBottariDetailFetchItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamBottariDetailFetchItemResponse>,
    @SerialName("personalItems")
    val personalItems: List<TeamBottariDetailFetchItemResponse>,
)

@Serializable
data class TeamBottariDetailAlarmFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("location")
    val location: TeamBottariDetailAlarmLocationResponse?,
    @SerialName("routine")
    val routine: TeamBottariDetailAlarmRoutineResponse,
)

@Serializable
data class TeamBottariDetailAlarmLocationResponse(
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
data class TeamBottariDetailAlarmRoutineResponse(
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
data class TeamBottariDetailFetchItemResponse(
    @SerialName("id")
    val itemId: Long,
    @SerialName("name")
    val name: String,
)
