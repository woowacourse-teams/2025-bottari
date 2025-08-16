package com.bottari.presentation.view.checklist.team.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.domain.usecase.team.RemindTeamBottariItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.model.TeamBottariStatusUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.model.TeamProductStatusItem
import kotlinx.coroutines.launch

class TeamBottariStatusViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamStatusUseCase: FetchTeamStatusUseCase,
    private val remindTeamBottariItemUseCase: RemindTeamBottariItemUseCase,
) : BaseViewModel<TeamBottariStatusUiState, TeamBottariStatusUiEvent>(
        TeamBottariStatusUiState(),
    ) {
    private val teamBottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    init {
        fetchTeamStatus()
    }

    fun selectItem(item: TeamBottariProductStatusUiModel) {
        updateState { copy(selectedProduct = item) }
    }

    fun remindTeamBottariItem() {
        viewModelScope.launch {
            remindTeamBottariItemUseCase
                .invoke(
                    currentState.selectedProduct?.id ?: throw IllegalArgumentException(),
                    currentState.selectedProduct?.type.toString(),
                ).onSuccess {
                    emitEvent(TeamBottariStatusUiEvent.SendRemindSuccess)
                }.onFailure {
                    emitEvent(TeamBottariStatusUiEvent.SendRemindFailure)
                }
        }
    }

    private fun fetchTeamStatus() {
        viewModelScope.launch {
            fetchTeamStatusUseCase
                .invoke(
                    teamBottariId,
                ).onSuccess { teamBottariStatus ->
                    val teamBottariStatusUiModel = teamBottariStatus.toUiModel()
                    val teamStatusListItems = generateTeamItemsList(teamBottariStatusUiModel)
                    updateState {
                        copy(
                            teamChecklistStatus = teamBottariStatusUiModel,
                            teamChecklistItems = teamStatusListItems,
                            selectedProduct =
                                teamStatusListItems
                                    .filter { it is TeamBottariProductStatusUiModel }
                                    .firstOrNull() as? TeamBottariProductStatusUiModel,
                        )
                    }
                }.onFailure {
                    emitEvent(TeamBottariStatusUiEvent.FetchTeamBottariStatusFailure)
                }
        }
    }

    private fun generateTeamItemsList(teamBottariStatusUiModel: TeamBottariStatusUiModel): List<TeamProductStatusItem> =
        buildList {
            add(TeamChecklistTypeUiModel(BottariItemTypeUiModel.SHARED))
            addAll(teamBottariStatusUiModel.sharedItems)
            add(TeamChecklistTypeUiModel(BottariItemTypeUiModel.ASSIGNED()))
            addAll(teamBottariStatusUiModel.assignedItems)
        }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamBottariStatusViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamStatusUseCase,
                        UseCaseProvider.remindTeamBottariItemUseCase,
                    )
                }
            }
    }
}
