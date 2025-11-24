package com.bottari.core.network.dto.team.member

import com.bottari.core.domain.model.bottari.item.BottariItemCount
import com.bottari.core.domain.model.bottari.item.ChecklistItem
import com.bottari.core.domain.model.member.Nickname
import com.bottari.core.domain.model.team.member.TeamMemberStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberStatusFetchResponse(
    @SerialName("memberId")
    val id: Long,
    @SerialName("teamMemberName")
    val nickname: String,
    @SerialName("isOwner")
    val isOwner: Boolean,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("sharedItems")
    val sharedItems: List<TeamChecklistItem>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamChecklistItem>,
) {
    fun toDomain(): TeamMemberStatus =
        TeamMemberStatus(
            id = id,
            nickname = Nickname(nickname),
            isHost = isOwner,
            itemCount =
                BottariItemCount(
                    totalQuantity = totalItemsCount,
                    checkedQuantity = checkedItemsCount,
                ),
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
        )

    @Serializable
    data class TeamChecklistItem(
        @SerialName("id")
        val id: Long,
        @SerialName("name")
        val name: String,
        @SerialName("isChecked")
        val isChecked: Boolean,
    ) {
        fun toDomain(): ChecklistItem =
            ChecklistItem(
                id = id,
                name = name,
                isChecked = isChecked,
            )
    }
}
