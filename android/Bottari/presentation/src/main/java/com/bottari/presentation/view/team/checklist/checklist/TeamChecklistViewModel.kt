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
import com.bottari.presentation.model.TeamBottariItemUiModel
import com.bottari.presentation.model.TeamChecklistParentUIModel
import com.bottari.presentation.view.team.checklist.checklist.adapter.TeamChecklistItem
import kotlinx.coroutines.launch

sealed interface TeamChecklistUiEvent

class TeamChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamBottariChecklistUseCase: FetchTeamChecklistUseCase,
    private val checkTeamBottariItemUseCase: CheckTeamBottariItemUseCase,
    private val unCheckTeamBottariItemUseCase: UnCheckTeamBottariItemUseCase,
) : BaseViewModel<TeamChecklistUiState, TeamChecklistUiEvent>(TeamChecklistUiState()) {
    val teamBottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    init {
        fetchTeamCheckList()
    }

    private fun generateExpandableList(
        currentExpandableItems: List<TeamChecklistItem>,
        categoryItems: Map<ChecklistCategory, List<TeamBottariItemUiModel>>,
    ): List<TeamChecklistItem> {
        val newExpandableList = mutableListOf<TeamChecklistItem>()

        ChecklistCategory.entries.forEach { category ->
            val itemsForCategory = categoryItems[category] ?: emptyList()
            val parent =
                currentExpandableItems
                    .filterIsInstance<TeamChecklistItem.Parent>()
                    .firstOrNull { it.teamChecklistParent.category == category }
                    ?.teamChecklistParent?.copy(children = itemsForCategory)
                    ?: TeamChecklistParentUIModel(category, itemsForCategory)

            newExpandableList.add(TeamChecklistItem.Parent(parent))
            if (parent.isExpanded) {
                newExpandableList.addAll(parent.children.map { TeamChecklistItem.Child(it) })
            }
        }
        return newExpandableList
    }

    private fun fetchTeamCheckList() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            fetchTeamBottariChecklistUseCase
                .invoke(teamBottariId)
                .onSuccess { checklistData ->
                    val newAllItems = checklistData.sharedItems.map { it.toUiModel(ChecklistCategory.PERSONAL) }
                    val newPointItems = checklistData.assignedItems.map { it.toUiModel(ChecklistCategory.ASSIGNED) }
                    val newPersonalItems = checklistData.personalItems.map { it.toUiModel(ChecklistCategory.PERSONAL) }

                    val newExpandableList =
                        generateExpandableList(
                            currentExpandableItems = currentState.expandableItems,
                            categoryItems =
                                mapOf(
                                    ChecklistCategory.SHARED to newAllItems,
                                    ChecklistCategory.ASSIGNED to newPointItems,
                                    ChecklistCategory.PERSONAL to newPersonalItems,
                                ),
                        )

                    updateState {
                        copy(
                            isLoading = false,
                            allItems = newAllItems,
                            pointItems = newPointItems,
                            personalItems = newPersonalItems,
                            expandableItems = newExpandableList,
                        )
                    }
                }.onFailure { exception ->
                    updateState { copy(isLoading = false) }
                }
        }
    }

    fun toggleItemChecked(itemId: Long) {
        viewModelScope.launch {
            val itemToToggle =
                currentState.allItems.firstOrNull { it.id == itemId }
                    ?: currentState.pointItems.firstOrNull { it.id == itemId }
                    ?: currentState.personalItems.firstOrNull { it.id == itemId }

            itemToToggle?.let { item ->
                updateState {
                    val newAllItems = allItems.toggleItemInList(itemId)
                    val newPointItems = pointItems.toggleItemInList(itemId)
                    val newPersonalItems = personalItems.toggleItemInList(itemId)

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
                        allItems = newAllItems,
                        pointItems = newPointItems,
                        personalItems = newPersonalItems,
                        expandableItems = newExpandableList,
                    )
                }

                val result =
                    if (item.isChecked) {
                        unCheckTeamBottariItemUseCase(itemId, item.category.toString())
                    } else {
                        checkTeamBottariItemUseCase(itemId, item.category.toString())
                    }

                result.onFailure {
                    updateState {
                        val newAllItems = allItems.toggleItemInList(itemId)
                        val newPointItems = pointItems.toggleItemInList(itemId)
                        val newPersonalItems = personalItems.toggleItemInList(itemId)

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
                            allItems = newAllItems,
                            pointItems = newPointItems,
                            personalItems = newPersonalItems,
                            expandableItems = newExpandableList,
                        )
                    }
                }
            }
        }
    }

    fun toggleParentExpanded(category: ChecklistCategory) {
        updateState {
            val updatedExpandableItems =
                currentState.expandableItems.map { item ->
                    if (item is TeamChecklistItem.Parent && item.teamChecklistParent.category == category) {
                        TeamChecklistItem.Parent(item.teamChecklistParent.copy(isExpanded = !item.teamChecklistParent.isExpanded))
                    } else {
                        item
                    }
                }

            val newExpandableList =
                generateExpandableList(
                    updatedExpandableItems,
                    mapOf(
                        ChecklistCategory.SHARED to currentState.allItems,
                        ChecklistCategory.ASSIGNED to currentState.pointItems,
                        ChecklistCategory.PERSONAL to currentState.personalItems,
                    ),
                )

            currentState.copy(expandableItems = newExpandableList)
        }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."

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

fun TeamBottariItemUiModel.toggle(): TeamBottariItemUiModel =
    TeamBottariItemUiModel(
        id = this.id,
        name = this.name,
        isChecked = this.isChecked.not(),
        category = this.category,
    )

private fun List<TeamBottariItemUiModel>.toggleItemInList(itemId: Long): List<TeamBottariItemUiModel> =
    this.map {
        if (it.id == itemId) it.toggle() else it
    }