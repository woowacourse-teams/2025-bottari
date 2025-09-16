package com.bottari.data.model.team.bottari.item.response

import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.model.team.member.TeamMember
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
    ) {
        fun toDomain(): TeamMember =
            TeamMember(
                memberId = memberId,
                nickname = name,
            )
    }

    fun toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.ASSIGNED(assignees.map { it.toDomain() }),
        )
}
