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
