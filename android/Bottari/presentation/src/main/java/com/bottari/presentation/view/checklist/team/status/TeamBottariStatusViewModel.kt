package com.bottari.presentation.view.checklist.team.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.domain.usecase.team.SendRemindByItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toAssignedUiModel
import com.bottari.presentation.mapper.TeamBottariMapper.toSharedUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.model.TeamProductStatusItem
import kotlinx.coroutines.launch

class TeamBottariStatusViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamStatusUseCase: FetchTeamStatusUseCase,
    private val sendRemindByItemUseCase: SendRemindByItemUseCase,
) : BaseViewModel<TeamBottariStatusUiState, TeamBottariStatusUiEvent>(
        TeamBottariStatusUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_ITEM_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    init {
        fetchTeamStatus()
    }

    fun selectItem(item: TeamBottariProductStatusUiModel) {
        updateState { copy(selectedProduct = item) }
    }

    fun sendRemindByItem() {
        viewModelScope.launch {
            sendRemindByItemUseCase(
                currentState.selectedProduct?.id ?: return@launch,
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
            fetchTeamStatusUseCase(teamBottariId)
                .onSuccess { teamBottariStatus ->
                    val sharedItems: List<TeamBottariProductStatusUiModel> =
                        teamBottariStatus.sharedItems.map { it.toSharedUiModel() }
                    val assignedItems =
                        teamBottariStatus.assignedItems.map { it.toAssignedUiModel() }
                    val teamStatusListItems = generateTeamItemsList(sharedItems, assignedItems)
                    updateState {
                        copy(
                            sharedItems = sharedItems,
                            assignedItems = assignedItems,
                            teamChecklistItems = teamStatusListItems,
                            selectedProduct =
                                teamStatusListItems
                                    .filterIsInstance<TeamBottariProductStatusUiModel>()
                                    .firstOrNull(),
                        )
                    }
                }.onFailure {
                    emitEvent(TeamBottariStatusUiEvent.FetchTeamBottariStatusFailure)
                }
        }
    }

    private fun generateTeamItemsList(
        sharedItems: List<TeamBottariProductStatusUiModel>,
        assignedItems: List<TeamBottariProductStatusUiModel>,
    ): List<TeamProductStatusItem> =
        buildList {
            add(TeamChecklistTypeUiModel(BottariItemTypeUiModel.SHARED))
            addAll(sharedItems)
            add(TeamChecklistTypeUiModel(BottariItemTypeUiModel.ASSIGNED()))
            addAll(assignedItems)
        }

    companion object {
        private const val KEY_ITEM_BOTTARI_ID = "KEY_ITEM_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_ITEM_BOTTARI_ID] = bottariId
                    TeamBottariStatusViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamStatusUseCase,
                        UseCaseProvider.sendRemindByItemUseCase,
                    )
                }
            }
    }
}
