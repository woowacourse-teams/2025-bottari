package com.bottari.feature.mybottari

import androidx.compose.runtime.Immutable
import com.bottari.feature.mybottari.component.MyBottariDialogType
import com.bottari.feature.mybottari.model.BottariUiModel
import com.bottari.feature.mybottari.model.MyBottariUiModel
import com.bottari.feature.mybottari.model.TeamBottariUiModel

@Immutable
data class MyBottariUiState(
    val isLoading: Boolean = false,
    val isPersonalFetched: Boolean = false,
    val isTeamFetched: Boolean = false,
    val personalBottaries: List<BottariUiModel> = emptyList(),
    val teamBottaries: List<TeamBottariUiModel> = emptyList(),
    val showDialogType: MyBottariDialogType? = null,
) {
    val isFetched: Boolean = isPersonalFetched && isTeamFetched
    val isPersonalEmpty: Boolean = isPersonalFetched && personalBottaries.isEmpty()
    val isTeamEmpty: Boolean = isTeamFetched && teamBottaries.isEmpty()
    val allBottaries: List<MyBottariUiModel> = teamBottaries + personalBottaries
    val isAllEmpty: Boolean = isFetched && allBottaries.isEmpty()
}
