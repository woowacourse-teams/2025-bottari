package com.bottari.data.model.team.member

import com.bottari.data.model.bottari.item.FetchChecklistResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberNameFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)

@Serializable
data class TeamMemberFetchResponse(
    @SerialName("inviteCode")
    val inviteCode: String,
    @SerialName("teamMemberCount")
    val teamMemberCount: Int,
    @SerialName("ownerName")
    val ownerName: String,
    @SerialName("teamMemberNames")
    val teamMemberNames: List<String>,
)

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
    val sharedItems: List<FetchChecklistResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<FetchChecklistResponse>,
)

@Serializable
data class TeamMemberStatusCheckedFetchResponse(
    @SerialName("name")
    val name: String,
    @SerialName("checked")
    val checked: Boolean,
)
