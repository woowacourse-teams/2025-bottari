package com.bottari.data.model.team.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamAssignedItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("assignees")
    val assignees: List<Assignee>,
) {
    @Serializable
    data class Assignee(
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("name")
        val name: String,
    )
}

@Serializable
data class FetchTeamPersonalItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)

@Serializable
data class FetchTeamSharedItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
