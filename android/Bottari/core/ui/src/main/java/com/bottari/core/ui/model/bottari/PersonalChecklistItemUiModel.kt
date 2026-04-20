package com.bottari.core.ui.model.bottari

import androidx.compose.runtime.Immutable
import com.bottari.core.domain.model.bottari.item.ChecklistItem
import com.bottari.core.ui.model.bottari.team.ChecklistItemUiModel

@Immutable
data class PersonalChecklistItemUiModel(
    override val id: Long,
    override val name: String,
    override val isChecked: Boolean,
) : ChecklistItemUiModel {
    companion object {
        fun fromDomain(checklistItem: ChecklistItem): PersonalChecklistItemUiModel =
            PersonalChecklistItemUiModel(
                id = checklistItem.id,
                name = checklistItem.name,
                isChecked = checklistItem.isChecked,
            )
    }
}
