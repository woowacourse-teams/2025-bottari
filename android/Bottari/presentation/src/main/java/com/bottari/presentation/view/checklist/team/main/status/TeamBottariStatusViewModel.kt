package com.bottari.presentation.view.checklist.team.main.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.TeamBottariStatus
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.FetchTeamStatusUseCase
import com.bottari.domain.usecase.team.SendRemindByItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toAssignedUiModel
import com.bottari.presentation.mapper.TeamBottariMapper.toSharedUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.model.TeamProductStatusItem
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TeamBottariStatusViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamStatusUseCase: FetchTeamStatusUseCase,
    private val sendRemindByItemUseCase: SendRemindByItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : BaseViewModel<TeamBottariStatusUiState, TeamBottariStatusUiEvent>(
        TeamBottariStatusUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_ITEM_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    val debouncedSendRemindByItem: (Unit) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { sendRemindByItem() }

    init {
        fetchTeamStatus()
        handleEvent()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    fun selectItem(item: TeamBottariProductStatusUiModel) {
        updateState { copy(selectedProduct = item) }
    }

    private fun sendRemindByItem() {
        val selectedProduct =
            currentState.selectedProduct ?: return emitEvent(
                TeamBottariStatusUiEvent.SendRemindFailure,
            )
        val itemId = selectedProduct.id
        val itemType = selectedProduct.type.toTypeString()

        launch {
            sendRemindByItemUseCase(itemId, itemType)
                .onSuccess { emitEvent(TeamBottariStatusUiEvent.SendRemindSuccess) }
                .onFailure { emitEvent(TeamBottariStatusUiEvent.SendRemindFailure) }
        }
    }

    private fun fetchTeamStatus() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamStatusUseCase(teamBottariId)
                .onSuccess { teamBottariStatus -> handleFetchTeamStatusSuccess(teamBottariStatus) }
                .onFailure { emitEvent(TeamBottariStatusUiEvent.FetchTeamBottariStatusFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase(teamBottariId)
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .debounce(DEBOUNCE_DELAY)
                .onEach { fetchTeamStatus() }
                .launchIn(this)
        }
    }

    private fun generateTeamItemsList(
        sharedItems: List<TeamBottariProductStatusUiModel>,
        assignedItems: List<TeamBottariProductStatusUiModel>,
    ): List<TeamProductStatusItem> =
        buildList {
            if (sharedItems.isNotEmpty()) {
                add(TeamChecklistTypeUiModel(BottariItemTypeUiModel.SHARED))
                addAll(sharedItems)
            }
            if (assignedItems.isNotEmpty()) {
                add(TeamChecklistTypeUiModel(BottariItemTypeUiModel.ASSIGNED()))
                addAll(assignedItems)
            }
        }

    private fun handleFetchTeamStatusSuccess(teamBottariStatus: TeamBottariStatus) {
        val sharedItems = teamBottariStatus.sharedItems.map { it.toSharedUiModel() }
        val assignedItems = teamBottariStatus.assignedItems.map { it.toAssignedUiModel() }
        val teamStatusListItems = generateTeamItemsList(sharedItems, assignedItems)
        val allProductItems = teamStatusListItems.filterIsInstance<TeamBottariProductStatusUiModel>()
        val selectedProduct =
            allProductItems.find { it.id == currentState.selectedProduct?.id } ?: allProductItems.firstOrNull()
        updateState {
            copy(
                sharedItems = sharedItems,
                assignedItems = assignedItems,
                teamChecklistItems = teamStatusListItems,
                selectedProduct = selectedProduct,
            )
        }
    }

    companion object {
        private const val KEY_ITEM_BOTTARI_ID = "KEY_ITEM_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 300L

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_ITEM_BOTTARI_ID] = bottariId
                    TeamBottariStatusViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamStatusUseCase,
                        UseCaseProvider.sendRemindByItemUseCase,
                        UseCaseProvider.connectTeamEventUseCase,
                        UseCaseProvider.disconnectTeamEventUseCase,
                    )
                }
            }
    }
}
