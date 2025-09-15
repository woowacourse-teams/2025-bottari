package com.bottari.data.model.team.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberStatusFetchResponse(
    @SerialName("memberId")
    val id: Long,
    @SerialName("teamMemberName")
    val nickname: String,
    @SerialName("isOwner")
    val isOwner: Boolean,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("sharedItems")
    val sharedItems: List<TeamMemberStatusBottariItemFetchResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamMemberStatusBottariItemFetchResponse>,
)

@Serializable
data class TeamMemberStatusBottariItemFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("isChecked")
    val isChecked: Boolean,
)
