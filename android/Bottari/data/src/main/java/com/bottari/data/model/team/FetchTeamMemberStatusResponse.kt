package com.bottari.data.model.team

import com.bottari.data.model.item.FetchChecklistResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamMemberStatusResponse(
    @SerialName("teamMemberName")
    val nickname: String,
    @SerialName("isOwner")
    val isOwner: Boolean,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("sharedItems")
    val sharedItems: List<FetchChecklistResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<FetchChecklistResponse>,
)
