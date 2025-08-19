package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariDetailResponse(
    @SerialName("id")
    val bottariId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: TeamAlarmResponse?,
    @SerialName("sharedItems")
    val sharedItems: List<BottariItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<BottariItemResponse>,
    @SerialName("personalItems")
    val personalItems: List<BottariItemResponse>,
)
