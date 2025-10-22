package com.bottari.presentation.compose.team.checklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.domain.model.tooltip.TooltipType
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.member.GetMemberIdUseCase
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.UncheckTeamBottariItemUseCase
import com.bottari.domain.usecase.tooltip.FetchTooltipStatusUseCase
import com.bottari.domain.usecase.tooltip.UpdateTooltipStatusUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel
import com.bottari.presentation.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeTeamChecklistViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchTeamBottariChecklistUseCase: FetchTeamChecklistUseCase,
    private val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase,
    private val unCheckTeamBottariItemUseCase: UncheckTeamBottariItemUseCase,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
    private val fetchTooltipStatusUseCase: FetchTooltipStatusUseCase,
    private val updateTooltipStatusUseCase: UpdateTooltipStatusUseCase,
) : FlowBaseViewModel<ComposeTeamChecklistUiState, ComposeTeamChecklistUiEvent>(
        ComposeTeamChecklistUiState(),
    ) {
    private val teamBottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private var memberId: Long = -1

    private val pendingCheckStatusMap =
        mutableMapOf<Pair<Long, BottariItemTypeUiModel>, TeamChecklistItemUiModel>()

    private val debouncedCheck: (List<TeamChecklistItemUiModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performItemCheck(items) }

    init {
        fetchTeamCheckList()
        fetchMemberId()
        handleEvent()
        checkIfTooltipWasDismissed()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    fun toggleTypeExpanded(type: BottariItemTypeUiModel) {
        val currentSections = currentState.sections

        val updatedSections =
            currentSections +
                mapOf(
                    type to !currentSections.getOrDefault(type, false),
                )

        updateState { copy(sections = updatedSections) }
    }

    fun toggleItemChecked(
        itemId: Long,
        type: BottariItemTypeUiModel,
    ) {
        val itemToToggle = findItemToToggle(itemId, type)

        itemToToggle?.let { item ->
            val toggledItem = item.toggle()
            val newItems =
                currentState.bottariItems.map { checklistItem ->
                    if (toggledItem.isSameItem(checklistItem)) return@map toggledItem
                    checklistItem
                }
            updateState {
                copy(
                    bottariItems = newItems,
                )
            }

            pendingCheckStatusMap[Pair(toggledItem.id, toggledItem.type)] = toggledItem
            debouncedCheck(pendingCheckStatusMap.values.toList())
        }
    }

    fun closeTooltip() {
        launch {
            updateTooltipStatusUseCase(TooltipType.TEAM)
            updateState { copy(isTooltipClosed = true) }
        }
    }

    private fun checkIfTooltipWasDismissed() {
        fetchTooltipStatusUseCase(TooltipType.TEAM)
            .onEach { state ->
                updateState { copy(isTooltipClosed = state) }
            }.launchIn(viewModelScope)
    }

    private fun fetchMemberId() {
        launch {
            memberId = getMemberIdUseCase().getOrDefault(-1)
        }
    }

    private fun TeamChecklistItemUiModel.toggle(): TeamChecklistItemUiModel = this.copy(isChecked = !this.isChecked)

    private fun fetchTeamCheckList() {
        launch {
            updateState { copy(isLoading = true) }

            fetchTeamBottariChecklistUseCase(teamBottariId)
                .onSuccess { checklistData ->
                    setTeamCheckList(checklistData)
                }.onFailure {
                    emitEvent(ComposeTeamChecklistUiEvent.FetchChecklistFailure)
                }
            updateState { copy(isLoading = false) }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase(teamBottariId)
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filterNot { eventData -> eventData.shouldIgnore() }
                .debounce(DEBOUNCE_DELAY)
                .onEach { fetchTeamCheckList() }
                .launchIn(this)
        }
    }

    private fun EventData.shouldIgnore(): Boolean =
        when (this) {
            is EventData.AssignedItemInfoCreate -> containMember(memberId).not()
            is EventData.AssignedItemInfoDelete -> containMember(memberId).not()
            is EventData.AssignedItemInfoChange -> containMember(memberId).not()
            is EventData.SharedItemInfoCreate,
            is EventData.SharedItemInfoDelete,
            -> false

            else -> true
        }

    private fun setTeamCheckList(checklistData: TeamBottariCheckList) {
        val newItems = checklistData.toUIModel()
        updateState {
            copy(
                bottariItems = newItems,
                originalBottariItems = newItems,
            )
        }
    }

    private fun TeamBottariCheckList.toUIModel() =
        this.sharedItems.map { item -> item.toTeamUiModel(BottariItemTypeUiModel.SHARED) } +
            this.assignedItems.map { item ->
                item.toTeamUiModel(
                    BottariItemTypeUiModel.ASSIGNED(),
                )
            } +
            this.personalItems.map { item ->
                item.toTeamUiModel(
                    BottariItemTypeUiModel.PERSONAL,
                )
            }

    private fun ChecklistItem.toTeamUiModel(type: BottariItemTypeUiModel) =
        TeamChecklistItemUiModel(
            id = id,
            name = name,
            isChecked = isChecked,
            type = type,
        )

    private fun findItemToToggle(
        itemId: Long,
        type: BottariItemTypeUiModel,
    ) = currentState.bottariItems.find { item ->
        item.isSameItem(
            itemId,
            type,
        )
    }

    private fun TeamChecklistItemUiModel.isSameItem(other: TeamChecklistItemUiModel): Boolean =
        this.id == other.id && this.type == other.type

    private fun TeamChecklistItemUiModel.isSameItem(
        itemId: Long,
        type: BottariItemTypeUiModel,
    ): Boolean = this.id == itemId && this.type == type

    private fun performItemCheck(items: List<TeamChecklistItemUiModel>) {
        launch {
            val originalItems = currentState.originalBottariItems
            val itemsToUpdate = mutableListOf<TeamChecklistItemUiModel>()

            val jobs =
                items.mapNotNull { pendingItem ->
                    val originalItem = originalItems.find { it.isSameItem(pendingItem) }
                    if (originalItem == null || originalItem.isChecked == pendingItem.isChecked) {
                        return@mapNotNull null
                    }
                    itemsToUpdate.add(pendingItem)
                    async { processItemCheck(pendingItem) }
                }

            jobs.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private fun updateOriginalItem(updatedItem: TeamChecklistItemUiModel) {
        val currentOriginals = currentState.originalBottariItems.toMutableList()
        val index = currentOriginals.indexOfFirst { it.isSameItem(updatedItem) }
        if (index != -1) {
            currentOriginals[index] = updatedItem
        }

        updateState { copy(originalBottariItems = currentOriginals) }
    }

    private suspend fun processItemCheck(item: TeamChecklistItemUiModel) {
        executeCheckUseCase(item)
            .onSuccess {
                updateOriginalItem(item)
            }.onFailure {
                emitEvent(ComposeTeamChecklistUiEvent.CheckItemFailure)
            }
    }

    private suspend fun executeCheckUseCase(item: TeamChecklistItemUiModel) =
        if (item.isChecked) {
            checkTeamBottariItemUseCase(item.id, item.type.toTypeString())
        } else {
            unCheckTeamBottariItemUseCase(item.id, item.type.toTypeString())
        }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 300L
    }
}
