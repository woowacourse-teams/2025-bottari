package com.bottari.data.model.teamItem

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
