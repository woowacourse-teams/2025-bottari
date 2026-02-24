package com.bottari.feature.team.checklist.checklist

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.bottari.common.util.debounce
import com.bottari.core.domain.model.bottari.item.ChecklistItem
import com.bottari.core.domain.model.event.EventData
import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.core.domain.model.tooltip.TooltipType
import com.bottari.core.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.core.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.core.domain.usecase.member.GetMemberIdUseCase
import com.bottari.core.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.core.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.core.domain.usecase.team.UncheckTeamBottariItemUseCase
import com.bottari.core.domain.usecase.tooltip.FetchTooltipStatusUseCase
import com.bottari.core.domain.usecase.tooltip.UpdateTooltipStatusUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.core.ui.model.bottari.team.TeamChecklistItemUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

@Stable
@HiltViewModel(assistedFactory = ComposeTeamChecklistViewModel.Factory::class)
class ComposeTeamChecklistViewModel @AssistedInject constructor(
    @Assisted private val teamBottariId: Long,
    private val fetchTeamBottariChecklistUseCase: FetchTeamChecklistUseCase,
    private val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase,
    private val unCheckTeamBottariItemUseCase: UncheckTeamBottariItemUseCase,
    private val getMemberIdUseCase: GetMemberIdUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
    private val fetchTooltipStatusUseCase: FetchTooltipStatusUseCase,
    private val updateTooltipStatusUseCase: UpdateTooltipStatusUseCase,
) : BaseViewModel<ComposeTeamChecklistUiState, ComposeTeamChecklistUiEvent>(
        ComposeTeamChecklistUiState(),
    ) {
    private var memberId: Long = -1

    private val pendingCheckStatusMap =
        mutableMapOf<Pair<Long, BottariItemTypeUiModel>, TeamChecklistItemUiModel>()

    private val debouncedCheck: (List<TeamChecklistItemUiModel>) -> Unit =
        viewModelScope.debounce(DEBOUNCE_DELAY) { items -> performItemCheck(items) }

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
            updateState { copy(bottariItems = newItems) }

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
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase()
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

    @AssistedFactory
    interface Factory {
        fun create(teamBottariId: Long): ComposeTeamChecklistViewModel
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
