package com.bottari.feature.team.checklist.checklist

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.core.ui.model.bottari.team.TeamChecklistItemUiModel

@Immutable
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
