package com.bottari.data.model.bottari

import com.bottari.data.model.alarm.AlarmResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchBottariesResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: AlarmResponse?,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
)

@Serializable
data class FetchBottariResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("alarm")
    val alarm: AlarmResponse?,
    @SerialName("items")
    val items: List<ItemResponse>,
    @SerialName("title")
    val title: String,
)

@Serializable
data class ItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
