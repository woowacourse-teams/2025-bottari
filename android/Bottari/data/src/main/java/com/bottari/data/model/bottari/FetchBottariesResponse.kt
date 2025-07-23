package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchBottariesResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarmResponse: AlarmResponse?,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
)
