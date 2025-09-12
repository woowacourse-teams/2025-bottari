package com.bottari.data.model.bottari.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveBottariItemsRequest(
    @SerialName("createItemNames")
    val createItemNames: List<String>,
    @SerialName("deleteItemIds")
    val deleteItemIds: List<Long>,
)
