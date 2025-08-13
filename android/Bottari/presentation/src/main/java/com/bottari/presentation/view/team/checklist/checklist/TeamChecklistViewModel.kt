package com.bottari.presentation.view.team.checklist.checklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.team.CheckTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamChecklistUseCase
import com.bottari.domain.usecase.team.UnCheckTeamBottariItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel
import com.bottari.presentation.model.TeamChecklistCategoryUIModel
import com.bottari.presentation.model.TeamChecklistItemUIModel
import com.bottari.presentation.util.debounce
import com.bottari.presentation.view.team.checklist.checklist.adapter.TeamChecklistItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

sealed interface TeamChecklistUiEvent {
    data object CheckItemFailure : TeamChecklistUiEvent
}

class TeamChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamBottariChecklistUseCase: FetchTeamChecklistUseCase,
    private val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase,
    private val unCheckTeamBottariItemUseCase: UnCheckTeamBottariItemUseCase,
) : BaseViewModel<TeamChecklistUiState, TeamChecklistUiEvent>(TeamChecklistUiState()) {
    private val teamBottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    private val pendingCheckStatusMap = mutableMapOf<Long, TeamChecklistItemUIModel>()

    private val debouncedCheck: (List<TeamChecklistItemUIModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performServerCheck(items) }

    init {
        fetchTeamCheckList()
    }

    private fun generateExpandableList(
        currentExpandableItems: List<TeamChecklistItem>,
        categoryItems: Map<ChecklistCategory, List<TeamChecklistItemUIModel>>,
    ): List<TeamChecklistItem> {
        val newExpandableList = mutableListOf<TeamChecklistItem>()

        ChecklistCategory.entries.forEach { category ->
            val categoryPageData =
                currentExpandableItems
                    .filterIsInstance<TeamChecklistItem.Category>()
                    .firstOrNull { it.teamChecklistCategory.category == category }
                    ?.teamChecklistCategory

            val newParent =
                (categoryPageData ?: TeamChecklistCategoryUIModel(category, emptyList()))
                    .copy(teamChecklistItems = categoryItems[category] ?: emptyList())

            newExpandableList.add(TeamChecklistItem.Category(newParent))
            if (newParent.isExpanded) {
                newExpandableList.addAll(newParent.teamChecklistItems.map { TeamChecklistItem.Item(it) })
            }
        }
        return newExpandableList
    }

    private fun fetchTeamCheckList() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            fetchTeamBottariChecklistUseCase(teamBottariId)
                .onSuccess { checklistData ->
                    val newSharedItems =
                        checklistData.sharedItems.map { it.toUiModel(ChecklistCategory.SHARED) }
                    val newAssignedItems =
                        checklistData.assignedItems.map { it.toUiModel(ChecklistCategory.ASSIGNED) }
                    val newPersonalItems =
                        checklistData.personalItems.map { it.toUiModel(ChecklistCategory.PERSONAL) }

                    val newExpandableList =
                        generateExpandableList(
                            currentExpandableItems = currentState.expandableItems,
                            categoryItems =
                                mapOf(
                                    ChecklistCategory.SHARED to newSharedItems,
                                    ChecklistCategory.ASSIGNED to newAssignedItems,
                                    ChecklistCategory.PERSONAL to newPersonalItems,
                                ),
                        )

                    updateState {
                        copy(
                            isLoading = false,
                            sharedItems = newSharedItems,
                            assignedItems = newAssignedItems,
                            personalItems = newPersonalItems,
                            expandableItems = newExpandableList,
                        )
                    }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                }
        }
    }

    fun toggleItemChecked(
        itemId: Long,
        itemCategory: ChecklistCategory,
    ) {
        val listToSearch =
            when (itemCategory) {
                ChecklistCategory.SHARED -> currentState.sharedItems
                ChecklistCategory.ASSIGNED -> currentState.assignedItems
                ChecklistCategory.PERSONAL -> currentState.personalItems
            }
        val itemToToggle = listToSearch.firstOrNull { it.id == itemId }

        itemToToggle?.let {
            val toggledItem = it.toggle()

            updateState {
                val newAllItems =
                    if (itemCategory == ChecklistCategory.SHARED) sharedItems.toggleItemInList(itemId) else sharedItems
                val newPointItems =
                    if (itemCategory == ChecklistCategory.ASSIGNED) {
                        assignedItems.toggleItemInList(
                            itemId,
                        )
                    } else {
                        assignedItems
                    }
                val newPersonalItems =
                    if (itemCategory == ChecklistCategory.PERSONAL) {
                        personalItems.toggleItemInList(
                            itemId,
                        )
                    } else {
                        personalItems
                    }

                val newExpandableList =
                    generateExpandableList(
                        currentExpandableItems = expandableItems,
                        categoryItems =
                            mapOf(
                                ChecklistCategory.SHARED to newAllItems,
                                ChecklistCategory.ASSIGNED to newPointItems,
                                ChecklistCategory.PERSONAL to newPersonalItems,
                            ),
                    )
                copy(
                    sharedItems = newAllItems,
                    assignedItems = newPointItems,
                    personalItems = newPersonalItems,
                    expandableItems = newExpandableList,
                )
            }

            pendingCheckStatusMap[toggledItem.id] = toggledItem
            debouncedCheck(pendingCheckStatusMap.values.toList())
        }
    }

    private fun performServerCheck(items: List<TeamChecklistItemUIModel>) {
        viewModelScope.launch {
            val jobs =
                items.map { item ->
                    async { processItemCheck(item) }
                }
            jobs.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private suspend fun processItemCheck(item: TeamChecklistItemUIModel) {
        val result =
            if (item.isChecked) {
                checkTeamBottariItemUseCase(item.id, item.category.toString())
            } else {
                unCheckTeamBottariItemUseCase(item.id, item.category.toString())
            }

        result.onFailure {
            emitEvent(TeamChecklistUiEvent.CheckItemFailure)
        }
    }

    fun toggleParentExpanded(category: ChecklistCategory) {
        updateState {
            val updatedExpandableItems =
                expandableItems.map { item ->
                    if (item is TeamChecklistItem.Category && item.teamChecklistCategory.category == category) {
                        TeamChecklistItem.Category(item.teamChecklistCategory.copy(isExpanded = !item.teamChecklistCategory.isExpanded))
                    } else {
                        item
                    }
                }

            val newExpandableList =
                generateExpandableList(
                    currentExpandableItems = updatedExpandableItems,
                    categoryItems =
                        mapOf(
                            ChecklistCategory.SHARED to sharedItems,
                            ChecklistCategory.ASSIGNED to assignedItems,
                            ChecklistCategory.PERSONAL to personalItems,
                        ),
                )
            copy(expandableItems = newExpandableList)
        }
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

fun TeamChecklistItemUIModel.toggle(): TeamChecklistItemUIModel = this.copy(isChecked = !this.isChecked)

private fun List<TeamChecklistItemUIModel>.toggleItemInList(itemId: Long): List<TeamChecklistItemUIModel> =
    this.map {
        if (it.id == itemId) it.toggle() else it
    }
