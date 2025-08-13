package com.bottari.data.model.team

import com.bottari.data.model.item.FetchChecklistResponse
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariChecklistResponse(
    val sharedItems: List<FetchChecklistResponse>,
    val assignedItems: List<FetchChecklistResponse>,
    val personalItems: List<FetchChecklistResponse>,
)
