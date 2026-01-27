package com.bottari.data.model.remote.bottari.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemsSaveRequest(
    @SerialName("createItemNames")
    val createItemNames: List<String>,
    @SerialName("deleteItemIds")
    val deleteItemIds: List<Long>,
)
