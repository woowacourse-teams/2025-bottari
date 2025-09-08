package com.bottari.data.model.bottari

import com.bottari.data.model.common.AlarmResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        val alarm: AlarmResponse?,
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
data class ItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
