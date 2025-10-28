package com.bottari.presentation.compose.team.checklist

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel

data class ComposeTeamChecklistUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val originalBottariItems: List<TeamChecklistItemUiModel> = emptyList(),
    val bottariItems: List<TeamChecklistItemUiModel> = emptyList(),
    val sections: Map<BottariItemTypeUiModel, Boolean> =
        mapOf(
            BottariItemTypeUiModel.SHARED to false,
            BottariItemTypeUiModel.ASSIGNED() to false,
            BottariItemTypeUiModel.PERSONAL to false,
        ),
    val isTooltipClosed: Boolean = true,
) {
    val isInitialLoading: Boolean = isLoading && isFetched.not()
    val totalQuantity: Int = bottariItems.size

    val checkedQuantity: Int = bottariItems.count { it.isChecked }

    val nonCheckedItems: List<TeamChecklistItemUiModel> = bottariItems.filterNot { it.isChecked }

    val isAllChecked: Boolean = bottariItems.isNotEmpty() && nonCheckedItems.isEmpty()
}
