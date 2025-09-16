package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.domain.model.bottari.item.ChecklistItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChecklistItemUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
) : Parcelable {
    companion object {
        fun fromDomain(checklistItem: ChecklistItem): ChecklistItemUiModel =
            ChecklistItemUiModel(
                id = checklistItem.id,
                name = checklistItem.name,
                isChecked = checklistItem.isChecked,
            )
    }
}
