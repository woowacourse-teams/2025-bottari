package com.bottari.data.model.team.item

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
data class SaveTeamAssignedItemRequest(
    @SerialName("name")
    val name: String,
    @SerialName("assigneeIds")
    val assigneeIds: List<Long>,
)

@Serializable
data class UpdateTeamItemCheckRequest(
    @SerialName("type")
    val type: String,
)

@Serializable
data class SendRemindByItemRequest(
    @SerialName("type")
    val type: String,
)
