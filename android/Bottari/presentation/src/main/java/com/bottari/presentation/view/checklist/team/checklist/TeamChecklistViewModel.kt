package com.bottari.presentation.view.checklist.team.checklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.UnCheckTeamBottariItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.TeamChecklistExpandableTypeUiModel
import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class TeamChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamBottariChecklistUseCase: FetchTeamChecklistUseCase,
    private val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase,
    private val unCheckTeamBottariItemUseCase: UnCheckTeamBottariItemUseCase,
) : BaseViewModel<TeamChecklistUiState, TeamChecklistUiEvent>(TeamChecklistUiState()) {
    private val teamBottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    private val pendingCheckStatusMap = mutableMapOf<Long, TeamChecklistProductUiModel>()

    private val debouncedCheck: (List<TeamChecklistProductUiModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performItemCheck(items) }

    init {
        fetchTeamCheckList()
    }

    fun toggleParentExpanded(type: BottariItemTypeUiModel) {
        val updatedExpandableItems =
            uiState.value?.expandableItems?.map { item ->
                if (item is TeamChecklistExpandableTypeUiModel && item.type == type) {
                    val reverseExpandType = item.copy(isExpanded = !item.isExpanded)
                    return@map reverseExpandType
                }
                item
            } ?: return

        val newExpandableList =
            generateExpandableTypeList(updatedExpandableItems, uiState.value?.items ?: return)
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
                uiState.value?.items?.map { checklistItem ->
                    if (toggledItem.isSameItem(checklistItem)) return@map toggledItem
                    checklistItem
                } ?: return
            val newExpandableList =
                uiState.value?.expandableItems?.toggleItemInList(toggledItem) ?: return

            updateState {
                copy(
                    items = newItems,
                    expandableItems = newExpandableList,
                )
            }

            pendingCheckStatusMap[toggledItem.id] = toggledItem
            debouncedCheck(pendingCheckStatusMap.values.toList())
        }
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

    private fun setTeamCheckList(checklistData: TeamBottariCheckList) {
        val newItems = checklistData.toUIModel()
        updateState { copy(items = newItems) }
        val newExpandableList =
            generateExpandableTypeList(currentState.expandableItems, newItems)
        updateState {
            copy(
                isLoading = false,
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
        .firstOrNull { it.type == type }

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
            val jobs =
                items.map { item ->
                    async { processItemCheck(item) }
                }
            jobs.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private suspend fun processItemCheck(item: TeamChecklistProductUiModel) {
        executeCheckUseCase(item).onFailure {
            emitEvent(TeamChecklistUiEvent.CheckItemFailure)
        }
    }

    private suspend fun executeCheckUseCase(item: TeamChecklistProductUiModel) =
        if (item.isChecked) {
            checkTeamBottariItemUseCase(item.id, item.type.toString())
        } else {
            unCheckTeamBottariItemUseCase(item.id, item.type.toString())
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
                    )
                }
            }
    }
}
