package com.bottari.presentation.view.checklist.team.main.checklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.event.DisconnectTeamEventUseCase
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.UnCheckTeamBottariItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.TeamChecklistExpandableTypeUiModel
import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TeamChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamBottariChecklistUseCase: FetchTeamChecklistUseCase,
    private val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase,
    private val unCheckTeamBottariItemUseCase: UnCheckTeamBottariItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
    private val disconnectTeamEventUseCase: DisconnectTeamEventUseCase,
) : BaseViewModel<TeamChecklistUiState, TeamChecklistUiEvent>(TeamChecklistUiState()) {
    private val teamBottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    private val pendingCheckStatusMap =
        mutableMapOf<Pair<Long, BottariItemTypeUiModel>, TeamChecklistProductUiModel>()

    private val debouncedCheck: (List<TeamChecklistProductUiModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performItemCheck(items) }

    init {
        fetchTeamCheckList()
        handleEvent()
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch { disconnectTeamEventUseCase() }
    }

    fun toggleParentExpanded(type: BottariItemTypeUiModel) {
        val updatedExpandableItems =
            currentState.expandableItems.map { item ->
                if (item is TeamChecklistExpandableTypeUiModel && item.type == type) {
                    val reverseExpandType = item.copy(isExpanded = !item.isExpanded)
                    return@map reverseExpandType
                }
                item
            }

        val newExpandableList =
            generateExpandableTypeList(updatedExpandableItems, currentState.bottariItems)
        updateState {
            copy(expandableItems = newExpandableList)
        }
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
            val newExpandableList =
                currentState.expandableItems.toggleItemInList(toggledItem)
            updateState {
                copy(
                    bottariItems = newItems,
                    expandableItems = newExpandableList,
                )
            }

            pendingCheckStatusMap[Pair(toggledItem.id, toggledItem.type)] = toggledItem
            debouncedCheck(pendingCheckStatusMap.values.toList())
        }
    }

    fun resetSwipeState() {
        updateState { copy(swipedItems = emptyList()) }
    }

    fun addSwipedItem(item: TeamChecklistItem) {
        updateState { copy(swipedItems = this.swipedItems + item) }
    }

    private fun List<TeamChecklistItem>.toggleItemInList(item: TeamChecklistProductUiModel): List<TeamChecklistItem> =
        this.map { listItem ->
            if (listItem.isSameItem(item).not()) return@map listItem
            item
        }

    private fun TeamChecklistProductUiModel.toggle(): TeamChecklistProductUiModel = this.copy(isChecked = !this.isChecked)

    private fun fetchTeamCheckList() {
        launch {
            updateState { copy(isLoading = true) }

            fetchTeamBottariChecklistUseCase(teamBottariId)
                .onSuccess { checklistData ->
                    setTeamCheckList(checklistData)
                }.onFailure {
                    emitEvent(TeamChecklistUiEvent.FetchChecklistFailure)
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
                .filter { eventData -> eventData !is EventData.TeamMemberCreate }
                .debounce(DEBOUNCE_DELAY)
                .onEach { fetchTeamCheckList() }
                .launchIn(this)
        }
    }

    private fun setTeamCheckList(checklistData: TeamBottariCheckList) {
        val newItems = checklistData.toUIModel()
        val newExpandableList =
            generateExpandableTypeList(currentState.expandableItems, newItems)
        updateState {
            copy(
                bottariItems = newItems,
                originalBottariItems = newItems,
                expandableItems = newExpandableList,
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
        TeamChecklistProductUiModel(
            id = id,
            name = name,
            isChecked = isChecked,
            type = type,
        )

    private fun generateExpandableTypeList(
        expandableItems: List<TeamChecklistItem>,
        items: List<TeamChecklistProductUiModel>,
    ) = generateExpandableList(
        currentExpandableItems = expandableItems,
        typeItems =
            mapOf(
                BottariItemTypeUiModel.SHARED to items.filter { it.type == BottariItemTypeUiModel.SHARED },
                BottariItemTypeUiModel.ASSIGNED() to items.filter { it.type == BottariItemTypeUiModel.ASSIGNED() },
                BottariItemTypeUiModel.PERSONAL to items.filter { it.type == BottariItemTypeUiModel.PERSONAL },
            ),
    )

    private fun generateExpandableList(
        currentExpandableItems: List<TeamChecklistItem>,
        typeItems: Map<BottariItemTypeUiModel, List<TeamChecklistProductUiModel>>,
    ): List<TeamChecklistItem> {
        val newExpandableList = mutableListOf<TeamChecklistItem>()
        val typeList =
            listOf(
                BottariItemTypeUiModel.SHARED,
                BottariItemTypeUiModel.ASSIGNED(),
                BottariItemTypeUiModel.PERSONAL,
            )
        typeList.forEach { type ->
            val typePageData =
                findItemsByType(currentExpandableItems, type)

            val newParent =
                (typePageData ?: TeamChecklistExpandableTypeUiModel(type, emptyList()))
                    .copy(teamChecklistItems = typeItems[type] ?: emptyList())

            newExpandableList.add(newParent)
            if (newParent.isExpanded) {
                newExpandableList.addAll(
                    newParent.teamChecklistItems.map { item ->
                        item
                    },
                )
            }
        }
        return newExpandableList
    }

    private fun findItemsByType(
        currentExpandableItems: List<TeamChecklistItem>,
        type: BottariItemTypeUiModel,
    ) = currentExpandableItems
        .filterIsInstance<TeamChecklistExpandableTypeUiModel>()
        .firstOrNull { item -> item.type == type }

    private fun findItemToToggle(
        itemId: Long,
        type: BottariItemTypeUiModel,
    ) = currentState.expandableItems.find { item ->
        item.isSameItem(
            itemId,
            type,
        )
    } as? TeamChecklistProductUiModel

    private fun TeamChecklistItem.isSameItem(other: TeamChecklistProductUiModel): Boolean =
        this is TeamChecklistProductUiModel && this.id == other.id && this.type == other.type

    private fun TeamChecklistItem.isSameItem(
        itemId: Long,
        type: BottariItemTypeUiModel,
    ): Boolean = this is TeamChecklistProductUiModel && this.id == itemId && this.type == type

    private fun performItemCheck(items: List<TeamChecklistProductUiModel>) {
        launch {
            val originalItems = currentState.originalBottariItems
            val itemsToUpdate = mutableListOf<TeamChecklistProductUiModel>()

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

    private fun updateOriginalItem(updatedItem: TeamChecklistProductUiModel) {
        val currentOriginals = currentState.originalBottariItems.toMutableList()
        val index = currentOriginals.indexOfFirst { it.isSameItem(updatedItem) }
        if (index != -1) {
            currentOriginals[index] = updatedItem
        }

        updateState { copy(originalBottariItems = currentOriginals) }
    }

    private suspend fun processItemCheck(item: TeamChecklistProductUiModel) {
        executeCheckUseCase(item)
            .onSuccess {
                updateOriginalItem(item)
            }.onFailure {
                emitEvent(TeamChecklistUiEvent.CheckItemFailure)
            }
    }

    private suspend fun executeCheckUseCase(item: TeamChecklistProductUiModel) =
        if (item.isChecked) {
            checkTeamBottariItemUseCase(item.id, item.type.toTypeString())
        } else {
            unCheckTeamBottariItemUseCase(item.id, item.type.toTypeString())
        }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 300L

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamChecklistViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamChecklistUseCase,
                        UseCaseProvider.checkTeamBottariItemUseCase,
                        UseCaseProvider.unCheckTeamBottariItemUseCase,
                        UseCaseProvider.connectTeamEventUseCase,
                        UseCaseProvider.disconnectTeamEventUseCase,
                    )
                }
            }
    }
}
