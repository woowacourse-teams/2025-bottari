package com.bottari.presentation.view.team.checklist.checklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.team.TeamBottariCheckList
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

            val newExpandableList = generateExpandableCategoryList(updatedExpandableItems, items)
            copy(expandableItems = newExpandableList)
        }
    }

    fun toggleItemChecked(
        itemId: Long,
        itemCategory: ChecklistCategory,
    ) {
        val listToSearch = currentState.expandableItems
        val itemToToggle =
            listToSearch.firstOrNull {
                it is TeamChecklistItem.Item && it.teamBottariItem.id == itemId &&
                    it.teamBottariItem.category == itemCategory
            }

        itemToToggle?.let {
            val toggledItem = (it as TeamChecklistItem.Item).teamBottariItem.toggle()
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
                newExpandableList.addAll(
                    newParent.teamChecklistItems.map {
                        TeamChecklistItem.Item(
                            it,
                        )
                    },
                )
            }
        }
        return newExpandableList
    }

    private fun fetchTeamCheckList() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            fetchTeamBottariChecklistUseCase(teamBottariId)
                .onSuccess { checklistData ->
                    val newItems = checklistData.toUIModel()
                    updateState { copy(items = newItems) }
                    val newExpandableList =
                        generateExpandableCategoryList(currentState.expandableItems, newItems)
                    updateState {
                        copy(
                            isLoading = false,
                            expandableItems = newExpandableList,
                        )
                    }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                }
        }
    }

    private fun generateExpandableCategoryList(
        expandableItems: List<TeamChecklistItem>,
        items: List<TeamChecklistItemUIModel>,
    ) = generateExpandableList(
        currentExpandableItems = expandableItems,
        categoryItems =
            mapOf(
                ChecklistCategory.SHARED to items.filter { it.category == ChecklistCategory.SHARED },
                ChecklistCategory.ASSIGNED to items.filter { it.category == ChecklistCategory.ASSIGNED },
                ChecklistCategory.PERSONAL to items.filter { it.category == ChecklistCategory.PERSONAL },
            ),
    )

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

    private fun TeamBottariCheckList.toUIModel() =
        this.sharedItems.map { it.toUiModel(ChecklistCategory.SHARED) } +
            this.assignedItems.map {
                it.toUiModel(
                    ChecklistCategory.ASSIGNED,
                )
            } +
            this.personalItems.map {
                it.toUiModel(
                    ChecklistCategory.PERSONAL,
                )
            }

    private fun TeamChecklistItemUIModel.toggle(): TeamChecklistItemUIModel = this.copy(isChecked = !this.isChecked)

    private fun List<TeamChecklistItem>.toggleItemInList(item: TeamChecklistItemUIModel): List<TeamChecklistItem> =
        this.map {
            if (it is TeamChecklistItem.Item && it.teamBottariItem.id == item.id && it.teamBottariItem.category == item.category) {
                TeamChecklistItem.Item(item)
            } else {
                it
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
