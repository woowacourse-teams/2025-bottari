package com.bottari.core.network.dto.team.bottari.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignedItemsUpdateRequest(
    @SerialName("name")
    val name: String,
    @SerialName("assigneeIds")
    val assigneeIds: List<Long>,
)
