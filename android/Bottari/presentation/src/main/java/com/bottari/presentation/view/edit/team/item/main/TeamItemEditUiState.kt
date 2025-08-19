package com.bottari.presentation.view.edit.team.item.main

import com.bottari.presentation.model.BottariItemTypeUiModel

data class TeamItemEditUiState(
    val itemInputText: String = "",
    val isAlreadyExist: Boolean = false,
    val currentTabType: BottariItemTypeUiModel,
    val sendCondition: Boolean = true,
) {
    val canSend: Boolean = itemInputText.isNotBlank() && isAlreadyExist.not() && sendCondition
}
