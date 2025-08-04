package com.bottari.presentation.view.checklist

import com.bottari.presentation.model.BottariItemUiModel

data class ChecklistUiState(
    val isLoading: Boolean = false,
    val bottariItems: List<BottariItemUiModel> = emptyList(),
) {
    val totalQuantity: Int
        get() = bottariItems.size

    val nonCheckedItems: List<BottariItemUiModel>
        get() = bottariItems.filter { item -> !item.isChecked }

    val checkedQuantity: Int
        get() = bottariItems.count { item -> item.isChecked }
}
