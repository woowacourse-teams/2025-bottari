package com.bottari.data.model.team.bottari

import com.bottari.data.model.alarm.AlarmFetchResponse
import com.bottari.data.model.team.member.TeamMemberStatusCheckedFetchResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariFetchDetailResponse(
    @SerialName("id")
    val bottariId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: AlarmFetchResponse?,
    @SerialName("sharedItems")
    val sharedItems: List<TeamBottariFetchDetailItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamBottariFetchDetailItemResponse>,
    @SerialName("personalItems")
    val personalItems: List<TeamBottariFetchDetailItemResponse>,
)

@Serializable
data class TeamBottariFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: AlarmFetchResponse?,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
    @SerialName("memberCount")
    val memberCount: Int,
)

@Serializable
data class FetchTeamBottariStatusResponse(
    @SerialName("sharedItems")
    val sharedItems: List<FetchTeamBottariStatusItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<FetchTeamBottariStatusItemResponse>,
)

@Serializable
data class TeamBottariFetchDetailItemResponse(
    @SerialName("id")
    val itemId: Long,
    @SerialName("name")
    val name: String,
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
