package com.bottari.data.model.team

import com.bottari.data.model.item.FetchChecklistResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariChecklistResponse(
    @SerialName("sharedItems")
    val sharedItems: List<FetchChecklistResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<FetchChecklistResponse>,
    @SerialName("personalItems")
    val personalItems: List<FetchChecklistResponse>,
)
