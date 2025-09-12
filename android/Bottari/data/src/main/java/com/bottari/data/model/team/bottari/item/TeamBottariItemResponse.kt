package com.bottari.data.model.team.bottari.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignedItemsFetchResponse(
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
data class PersonalItemsFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)

@Serializable
data class SharedItemsFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
