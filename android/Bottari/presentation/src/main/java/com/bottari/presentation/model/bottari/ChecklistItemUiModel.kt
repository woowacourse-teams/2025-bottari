package com.bottari.presentation.model.bottari

import com.bottari.domain.model.bottari.item.ChecklistItem

data class ChecklistItemUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
) {
    companion object {
        fun fromDomain(checklistItem: ChecklistItem): ChecklistItemUiModel =
            ChecklistItemUiModel(
                id = checklistItem.id,
                name = checklistItem.name,
                isChecked = checklistItem.isChecked,
            )
    }
}
