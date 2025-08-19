package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveTeamBottariAssignedItemRequest(
    @SerialName("name")
    val name: String,
    @SerialName("assigneeIds")
    val assigneeIds: List<Long>,
)
