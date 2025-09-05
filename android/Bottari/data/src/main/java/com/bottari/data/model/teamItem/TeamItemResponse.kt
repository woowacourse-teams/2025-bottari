package com.bottari.data.model.teamItem

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface TeamItemMinimal {
    val id: Long
    val name: String
}

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
    override val id: Long,
    @SerialName("name")
    override val name: String,
) : TeamItemMinimal

@Serializable
data class FetchTeamSharedItemResponse(
    @SerialName("id")
    override val id: Long,
    @SerialName("name")
    override val name: String,
) : TeamItemMinimal
