package com.bottari.data.model.remote.team.bottari.item.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignedItemsCreateRequest(
    @SerialName("name")
    val name: String,
    @SerialName("memberIds")
    val memberIds: List<Long>,
)
