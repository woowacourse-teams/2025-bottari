package com.bottari.presentation.model.template

import com.bottari.domain.model.bottari.Bottari
import com.bottari.presentation.model.bottari.BottariItemUiModel

data class SelectableBottariUiModel(
    val id: Long,
    val title: String,
    val items: List<BottariItemUiModel>,
    val isSelected: Boolean,
) {
    companion object {
        fun fromDomain(bottari: Bottari): SelectableBottariUiModel =
            SelectableBottariUiModel(
                id = bottari.id,
                title = bottari.title,
                isSelected = false,
                items = bottari.items.map { item -> BottariItemUiModel.fromDomain(item) },
            )
    }
}
