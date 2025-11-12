package com.bottari.presentation.compose.edit.personal.item

import androidx.compose.runtime.Immutable
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

@Immutable
data class PersonalItemEditUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val bottariId: Long = 0,
    val itemName: String = "",
    val items: List<PersonalChecklistItemUiModel> = emptyList(),
) {
    val isEmpty: Boolean = !isLoading && isFetched && items.isEmpty()
    private val isDuplicate: Boolean =
        isEmpty.not() && items.any { item -> item.name == itemName.trim() }
    private val isExceed: Boolean = itemName.length > MAX_ITEM_NAME_LENGTH
    val isInvalidItem: Boolean = isDuplicate || isExceed
    val isSavable: Boolean = itemName.isNotBlank() && isInvalidItem.not()

    companion object {
        private const val MAX_ITEM_NAME_LENGTH = 20
    }
}
