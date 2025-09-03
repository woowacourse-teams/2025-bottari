package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamBottariAssignedItemRequest(
    @SerialName("name")
    val name: String,
    @SerialName("memberIds")
    val memberIds: List<Long>,
)

@Serializable
data class CreateTeamBottariPersonalItemRequest(
    @SerialName("name")
    val name: String,
)

@Serializable
data class CreateTeamBottariSharedItemRequest(
    @SerialName("name")
    val name: String,
)

@Serializable
data class DeleteTeamBottariItemRequest(
    @SerialName("type")
    val type: String,
)

@Serializable
data class SaveTeamBottariAssignedItemRequest(
    @SerialName("name")
    val name: String,
    @SerialName("assigneeIds")
    val assigneeIds: List<Long>,
)

@Serializable
data class TeamItemTypeRequest(
    @SerialName("type")
    val type: String,
)
