package com.bottari.presentation.model

import com.bottari.domain.model.bottari.Bottari

data class MyBottariUiModel(
    val id: Long,
    val title: String,
    val isSelected: Boolean,
    val items: List<BottariItemUiModel>,
) {
    companion object {
        fun fromDomain(bottari: Bottari): MyBottariUiModel =
            MyBottariUiModel(
                id = bottari.id,
                title = bottari.title,
                isSelected = false,
                items = bottari.items.map { item -> BottariItemUiModel.fromDomain(item) },
            )
    }
}
