package com.bottari.presentation.model.template

import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

data class SelectableBottariUiModel(
    val id: Long,
    val title: String,
    val items: List<PersonalChecklistItemUiModel>,
    val isSelected: Boolean,
) {
    companion object {
        fun fromDomain(bottari: PersonalBottari): SelectableBottariUiModel =
            SelectableBottariUiModel(
                id = bottari.id,
                title = bottari.title,
                isSelected = false,
                items = bottari.items.map(PersonalChecklistItemUiModel::fromDomain),
            )
    }
}
