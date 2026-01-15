package com.bottari.feature.team.checklist.item

import androidx.compose.runtime.Immutable
import com.bottari.core.ui.model.bottari.team.TeamBottariUiModelStatus

@Immutable
data class ComposeTeamBottariItemStatusUiState(
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val items: List<TeamBottariUiModelStatus> = listOf(),
    val selectedProduct: TeamBottariUiModelStatus? = null,
    val myNickname: String = "",
) {
    val isInitialLoading: Boolean = isLoading && isFetched.not()
    private val totalCount =
        items.sumOf { it.totalItemsCount }
    private val checkedCount =
        items.sumOf { it.checkItemsCount }

    val isOnlyMeUnchecked = selectedProduct?.uncheckedMember == listOf(myNickname)
    val completedItems =
        items.count { it.checkItemsCount == it.totalItemsCount }
    val checkedProgress =
        if (totalCount > 0) {
            ((checkedCount.toFloat() / totalCount.toFloat()) * 100).toInt()
        } else {
            0
        }
}
