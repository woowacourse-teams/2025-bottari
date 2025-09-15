package com.bottari.data.model.team.bottari.item.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariItemChecklistFetchResponse(
    @SerialName("sharedItems")
    val sharedItems: List<TeamChecklistItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamChecklistItemResponse>,
    @SerialName("personalItems")
    val personalItems: List<TeamChecklistItemResponse>,
)

@Serializable
data class TeamChecklistItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("isChecked")
    val isChecked: Boolean,
)
