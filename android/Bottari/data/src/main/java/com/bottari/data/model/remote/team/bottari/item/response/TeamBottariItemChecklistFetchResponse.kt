package com.bottari.data.model.remote.team.bottari.item.response

import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariItemChecklistFetchResponse(
    @SerialName("sharedItems")
    val sharedItems: List<TeamChecklistItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamChecklistItemResponse>,
    @SerialName("personalItems")
    val personalItems: List<TeamChecklistItemResponse>,
) {
    fun toDomain(): TeamBottariCheckList =
        TeamBottariCheckList(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
            personalItems = personalItems.map { it.toDomain() },
        )
}

@Serializable
data class TeamChecklistItemResponse(
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
