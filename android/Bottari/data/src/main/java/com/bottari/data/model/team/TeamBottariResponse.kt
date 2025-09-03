package com.bottari.data.model.team

import com.bottari.data.model.bottari.AlarmResponse
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

@Serializable
data class FetchTeamBottariDetailResponse(
    @SerialName("id")
    val bottariId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: TeamAlarmResponse?,
    @SerialName("sharedItems")
    val sharedItems: List<TeamBottariItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamBottariItemResponse>,
    @SerialName("personalItems")
    val personalItems: List<TeamBottariItemResponse>,
)

@Serializable
data class FetchTeamBottariResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarmResponse: AlarmResponse?,
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
    val sharedItems: List<TeamProductStatusResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamProductStatusResponse>,
)

@Serializable
data class TeamBottariItemResponse(
    @SerialName("id")
    val itemId: Long,
    @SerialName("name")
    val name: String,
)

@Serializable
data class TeamProductStatusResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("memberCheckStatus")
    val memberCheckStatus: List<MemberCheckStatusResponse>,
    @SerialName("checkItemsCount")
    val checkItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
)
