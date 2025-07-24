package com.bottari.data.model.bottari
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("alarm")
    val alarm: AlarmResponse?,
    @SerialName("items")
    val items: List<ItemResponse>,
    @SerialName("title")
    val title: String,
)
