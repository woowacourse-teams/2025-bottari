package com.bottari.presentation.model.bottari

import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.presentation.model.bottari.team.ChecklistItemUiModel

data class PersonalChecklistItemUiModel(
    override val id: Long,
    override val name: String,
    override val isChecked: Boolean,
): ChecklistItemUiModel {
    companion object {
        fun fromDomain(checklistItem: ChecklistItem): PersonalChecklistItemUiModel =
            PersonalChecklistItemUiModel(
                id = checklistItem.id,
                name = checklistItem.name,
                isChecked = checklistItem.isChecked,
            )
    }
}
