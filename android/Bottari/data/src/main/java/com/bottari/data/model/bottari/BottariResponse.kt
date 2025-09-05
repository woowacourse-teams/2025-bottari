package com.bottari.data.model.bottari

import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
sealed interface BottariResponse {
    val id: Long
    val title: String

    @Serializable
    data class FetchBottariesResponse(
        @SerialName("id")
        override val id: Long,
        @SerialName("title")
        override val title: String,
        @SerialName("alarm")
        val alarmResponse: AlarmResponse?,
        @SerialName("checkedItemsCount")
        val checkedItemsCount: Int,
        @SerialName("totalItemsCount")
        val totalItemsCount: Int,
    ) : BottariResponse

    @Serializable
    data class FetchBottariResponse(
        @SerialName("id")
        override val id: Long,
        @SerialName("alarm")
        val alarm: AlarmResponse?,
        @SerialName("items")
        val items: List<ItemResponse>,
        @SerialName("title")
        override val title: String,
    ) : BottariResponse
}

@Serializable
data class AlarmResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("location")
    val location: LocationResponse?,
    @SerialName("routine")
    val routine: RoutineResponse,
)

@Serializable
data class LocationResponse(
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
data class RoutineResponse(
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
data class ItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
