package com.bottari.presentation.view.checklist.team.checklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.UnCheckTeamBottariItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.TeamChecklistItemUiModel
import com.bottari.presentation.model.TeamChecklistRowUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel
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

    private val pendingCheckStatusMap = mutableMapOf<Long, TeamChecklistItemUiModel>()

    private val debouncedCheck: (List<TeamChecklistItemUiModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performItemCheck(items) }

    init {
        fetchTeamCheckList()
    }

    fun toggleParentExpanded(type: ChecklistType) {
        updateState {
            val updatedExpandableItems =
                expandableItems.map { item ->
                    if (item is TeamChecklistTypeUiModel && item.type == type) {
                        item.copy(isExpanded = !item.isExpanded)
                    } else {
                        item
                    }
                }

            val newExpandableList = generateExpandableTypeList(updatedExpandableItems, items)
            copy(expandableItems = newExpandableList)
        }
    }

    fun toggleItemChecked(
        itemId: Long,
        type: ChecklistType,
    ) {
        val itemToToggle =
            findItemToToggle(itemId, type)

        itemToToggle?.let { item ->
            val toggledItem = item.toggle()
            updateState {
                val newExpandableList = expandableItems.toggleItemInList(toggledItem)
                copy(
                    expandableItems = newExpandableList,
                )
            }

            pendingCheckStatusMap[toggledItem.id] = toggledItem
            debouncedCheck(pendingCheckStatusMap.values.toList())
        }
    }

    private fun List<TeamChecklistRowUiModel>.toggleItemInList(item: TeamChecklistItemUiModel): List<TeamChecklistRowUiModel> =
        this.map { row ->
            if (row.isSameItem(item)) {
                item
            } else {
                row
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
                    updateState { copy(isLoading = false) }
                    emitEvent(TeamChecklistUiEvent.FetchChecklistFailure)
                }
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
        this.sharedItems.map { it.toTeamUiModel(ChecklistType.SHARED) } +
            this.assignedItems.map {
                it.toTeamUiModel(
                    ChecklistType.ASSIGNED,
                )
            } +
            this.personalItems.map {
                it.toTeamUiModel(
                    ChecklistType.PERSONAL,
                )
            }

    private fun BottariItem.toTeamUiModel(type: ChecklistType) =
        TeamChecklistItemUiModel(
            id = id,
            name = name,
            isChecked = isChecked,
            type = type,
        )

    private fun generateExpandableTypeList(
        expandableItems: List<TeamChecklistRowUiModel>,
        items: List<TeamChecklistItemUiModel>,
    ) = generateExpandableList(
        currentExpandableItems = expandableItems,
        typeItems =
            mapOf(
                ChecklistType.SHARED to items.filter { it.type == ChecklistType.SHARED },
                ChecklistType.ASSIGNED to items.filter { it.type == ChecklistType.ASSIGNED },
                ChecklistType.PERSONAL to items.filter { it.type == ChecklistType.PERSONAL },
            ),
    )

    private fun generateExpandableList(
        currentExpandableItems: List<TeamChecklistRowUiModel>,
        typeItems: Map<ChecklistType, List<TeamChecklistItemUiModel>>,
    ): List<TeamChecklistRowUiModel> {
        val newExpandableList = mutableListOf<TeamChecklistRowUiModel>()

        ChecklistType.entries.forEach { type ->
            val typePageData =
                findItemsByType(currentExpandableItems, type)

            val newParent =
                (typePageData ?: TeamChecklistTypeUiModel(type, emptyList()))
                    .copy(teamChecklistItems = typeItems[type] ?: emptyList())

            newExpandableList.add(newParent)
            if (newParent.isExpanded) {
                newExpandableList.addAll(
                    newParent.teamChecklistItems.map {
                        it
                    },
                )
            }
        }
        return newExpandableList
    }

    private fun findItemsByType(
        currentExpandableItems: List<TeamChecklistRowUiModel>,
        type: ChecklistType,
    ) = currentExpandableItems
        .filterIsInstance<TeamChecklistTypeUiModel>()
        .firstOrNull { it.type == type }

    private fun findItemToToggle(
        itemId: Long,
        type: ChecklistType,
    ) = currentState.expandableItems.find { it.isSameItem(itemId, type) } as? TeamChecklistItemUiModel

    private fun TeamChecklistRowUiModel.isSameItem(other: TeamChecklistItemUiModel): Boolean =
        this is TeamChecklistItemUiModel && this.id == other.id && this.type == other.type

    private fun TeamChecklistRowUiModel.isSameItem(
        itemId: Long,
        type: ChecklistType,
    ): Boolean = this is TeamChecklistItemUiModel && this.id == itemId && this.type == type

    private fun performItemCheck(items: List<TeamChecklistItemUiModel>) {
        launch {
            val jobs =
                items.map { item ->
                    async { processItemCheck(item) }
                }
            jobs.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private suspend fun processItemCheck(item: TeamChecklistItemUiModel) {
        val result = executeCheckUseCase(item)
        result.onFailure {
            emitEvent(TeamChecklistUiEvent.CheckItemFailure)
        }
    }

    private suspend fun executeCheckUseCase(item: TeamChecklistItemUiModel) =
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
