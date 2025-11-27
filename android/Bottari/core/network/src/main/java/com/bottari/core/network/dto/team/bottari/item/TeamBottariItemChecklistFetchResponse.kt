package com.bottari.core.network.dto.team.bottari.item

import com.bottari.core.domain.model.bottari.item.ChecklistItem
import com.bottari.core.domain.model.team.bottari.TeamBottariCheckList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariItemChecklistFetchResponse(
    @SerialName("sharedItems")
    val sharedItems: List<TeamChecklistItem>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamChecklistItem>,
    @SerialName("personalItems")
    val personalItems: List<TeamChecklistItem>,
) {
    fun toDomain(): TeamBottariCheckList =
        TeamBottariCheckList(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
            personalItems = personalItems.map { it.toDomain() },
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
