package com.bottari.data.model.team.bottari.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignedItemsCreateRequest(
    @SerialName("name")
    val name: String,
    @SerialName("memberIds")
    val memberIds: List<Long>,
)

@Serializable
data class PersonalItemsCreateRequest(
    @SerialName("name")
    val name: String,
)

@Serializable
data class SharedItemsCreateRequest(
    @SerialName("name")
    val name: String,
)

@Serializable
data class AssignedItemsUpdateRequest(
    @SerialName("name")
    val name: String,
    @SerialName("assigneeIds")
    val assigneeIds: List<Long>,
)

@Serializable
data class TeamBottariItemDeleteRequest(
    @SerialName("type")
    val type: String,
)

@Serializable
data class TeamBottariItemCheckUpdateRequest(
    @SerialName("type")
    val type: String,
)

@Serializable
data class TeamBottariItemUnCheckUpdateRequest(
    @SerialName("type")
    val type: String,
)

@Serializable
data class TeamBottariItemRemindRequest(
    @SerialName("type")
    val type: String,
)

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
