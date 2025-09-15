package com.bottari.data.model.team.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariStatusResponse(
    @SerialName("sharedItems")
    val sharedItems: List<FetchTeamBottariStatusItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<FetchTeamBottariStatusItemResponse>,
)

@Serializable
data class FetchTeamBottariStatusItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("memberCheckStatus")
    val memberCheckStatus: List<TeamMemberStatusCheckedFetchResponse>,
    @SerialName("checkItemsCount")
    val checkItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
)

@Serializable
data class TeamMemberStatusCheckedFetchResponse(
    @SerialName("name")
    val name: String,
    @SerialName("checked")
    val checked: Boolean,
)
