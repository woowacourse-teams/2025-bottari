package com.bottari.presentation.view.checklist.team.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import kotlinx.coroutines.launch

data class TeamStatusUiState(
    val isLoading: Boolean = false,
    val teamChecklistStatus: TeamBottariStatusUiModel? = null,
    val item: TeamProductStatusUiModel? = null,
    val teamChecklistItems: List<TeamProductStatusItem> = listOf(),
)

sealed interface TeamStatusUiEvent {
    data object FetchChecklistFailure : TeamStatusUiEvent

    data object CheckItemFailure : TeamStatusUiEvent
}

class TeamStatusViewModel(
    private val stateHandle: SavedStateHandle,
    private val fetchTeamStatusUseCase: FetchTeamStatusUseCase,
) : BaseViewModel<TeamStatusUiState, TeamStatusUiEvent>(
        TeamStatusUiState(),
    ) {
    init {
        fetchTeamStatus()
    }

    private fun fetchTeamStatus() {
        viewModelScope.launch {
            fetchTeamStatusUseCase
                .invoke(
                    stateHandle[KEY_BOTTARI_ID] ?: throw IllegalArgumentException(
                        ERROR_REQUIRE_BOTTARI_ID,
                    ),
                ).onSuccess { teamBottariStatus ->
                    val teamBottariStatusUiModel = teamBottariStatus.toUiModel()
                    val teamStatusListItems = generateTeamItemsList(teamBottariStatusUiModel)
                    updateState {
                        copy(
                            teamChecklistStatus = teamBottariStatusUiModel,
                            teamChecklistItems = teamStatusListItems,
                            item =
                                teamStatusListItems
                                    .filter { it is TeamProductStatusUiModel }
                                    .firstOrNull() as? TeamProductStatusUiModel,
                        )
                    }
                }.onFailure {
                    emitEvent(TeamStatusUiEvent.FetchChecklistFailure)
                }
        }
    }

    fun selectItem(item: TeamProductStatusUiModel) {
        updateState { copy(item = item) }
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
                    TeamStatusViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamStatusUseCase,
                    )
                }
            }
    }
}
