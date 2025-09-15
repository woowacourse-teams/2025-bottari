package com.bottari.data.model.bottari

import com.bottari.data.model.alarm.response.AlarmFetchResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariesFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: AlarmFetchResponse?,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
)

@Serializable
data class BottariFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("alarm")
    val alarm: AlarmFetchResponse?,
    @SerialName("items")
    val items: List<BottariItemFetchResponse>,
    @SerialName("title")
    val title: String,
)

@Serializable
data class BottariItemFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
