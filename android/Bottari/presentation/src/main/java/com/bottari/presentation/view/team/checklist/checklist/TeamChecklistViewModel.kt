package com.bottari.presentation.view.team.checklist.checklist

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.TeamChecklistParentUIModel

sealed interface TeamChecklistUiEvent

class TeamChecklistViewModel : BaseViewModel<TeamChecklistUiState, TeamChecklistUiEvent>(TeamChecklistUiState()) {
    private val categoryItemsMap: Map<ChecklistCategory, List<BottariItemUiModel>> =
        mapOf(
            ChecklistCategory.COMMON to fixtureList1,
            ChecklistCategory.RESPONSIBLE to fixtureList2,
            ChecklistCategory.PERSONAL to fixtureList3,
        )

    init {
        fetchTeamCheckList()
    }

    private fun generateExpandableList(
        currentExpandableItems: List<Any>,
        categoryItems: Map<ChecklistCategory, List<BottariItemUiModel>>,
    ): List<Any> {
        val newExpandableList = mutableListOf<Any>()

        ChecklistCategory.entries.forEach { category ->
            val itemsForCategory = categoryItems[category] ?: emptyList()
            val parent =
                currentExpandableItems
                    .filterIsInstance<TeamChecklistParentUIModel>()
                    .firstOrNull { it.category == category }
                    ?.copy(children = itemsForCategory)
                    ?: TeamChecklistParentUIModel(category, itemsForCategory)

            newExpandableList.add(parent)
            if (parent.isExpanded) {
                newExpandableList.addAll(parent.children)
            }
        }
        return newExpandableList
    }

    private fun fetchTeamCheckList() {
        updateState {
            val initialExpandableItems =
                generateExpandableList(
                    emptyList(),
                    categoryItemsMap,
                )

            copy(
                allItems = categoryItemsMap[ChecklistCategory.COMMON] ?: emptyList(),
                pointItems = categoryItemsMap[ChecklistCategory.RESPONSIBLE] ?: emptyList(),
                personalItems = categoryItemsMap[ChecklistCategory.PERSONAL] ?: emptyList(),
                expandableItems = initialExpandableItems,
            )
        }
    }

    fun toggleItemChecked(itemId: Long) {
        updateState {
            val newState =
                when {
                    currentState.allItems.any { it.id == itemId } -> {
                        currentState.copy(allItems = currentState.allItems.toggleItemInList(itemId))
                    }
                    currentState.pointItems.any { it.id == itemId } -> {
                        currentState.copy(pointItems = currentState.pointItems.toggleItemInList(itemId))
                    }
                    currentState.personalItems.any { it.id == itemId } -> {
                        currentState.copy(personalItems = currentState.personalItems.toggleItemInList(itemId))
                    }
                    else -> currentState
                }

            val newExpandableList =
                generateExpandableList(
                    currentState.expandableItems,
                    mapOf(
                        ChecklistCategory.COMMON to newState.allItems,
                        ChecklistCategory.RESPONSIBLE to newState.pointItems,
                        ChecklistCategory.PERSONAL to newState.personalItems,
                    ),
                )

            newState.copy(expandableItems = newExpandableList)
        }
    }

    fun toggleParentExpanded(category: ChecklistCategory) {
        updateState {
            val updatedExpandableItems =
                currentState.expandableItems.map { item ->
                    if (item is TeamChecklistParentUIModel && item.category == category) {
                        item.copy(isExpanded = !item.isExpanded)
                    } else {
                        item
                    }
                }

            val newExpandableList =
                generateExpandableList(
                    updatedExpandableItems,
                    mapOf(
                        ChecklistCategory.COMMON to currentState.allItems,
                        ChecklistCategory.RESPONSIBLE to currentState.pointItems,
                        ChecklistCategory.PERSONAL to currentState.personalItems,
                    ),
                )

            currentState.copy(expandableItems = newExpandableList)
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TeamChecklistViewModel()
                }
            }
    }
}

fun BottariItemUiModel.toggle(): BottariItemUiModel =
    BottariItemUiModel(
        id = this.id,
        name = this.name,
        isChecked = this.isChecked.not(),
    )

private fun List<BottariItemUiModel>.toggleItemInList(itemId: Long): List<BottariItemUiModel> =
    this.map {
        if (it.id == itemId) it.toggle() else it
    }

val fixtureList1 =
    listOf(
        BottariItemUiModel(id = 1, name = "여권", isChecked = false),
        BottariItemUiModel(id = 2, name = "항공권 (E-티켓)", isChecked = false),
        BottariItemUiModel(id = 3, name = "지갑 및 현금", isChecked = false),
        BottariItemUiModel(id = 4, name = "스마트폰", isChecked = false),
        BottariItemUiModel(id = 5, name = "충전기 및 보조배터리", isChecked = false),
        BottariItemUiModel(id = 6, name = "상비약", isChecked = false),
        BottariItemUiModel(id = 7, name = "세면도구", isChecked = false),
        BottariItemUiModel(id = 8, name = "갈아입을 옷", isChecked = false),
        BottariItemUiModel(id = 9, name = "속옷 및 양말", isChecked = false),
        BottariItemUiModel(id = 10, name = "여행용 어댑터", isChecked = false),
    )

val fixtureList2 =
    listOf(
        BottariItemUiModel(id = 11, name = "목베개", isChecked = false),
        BottariItemUiModel(id = 12, name = "카메라", isChecked = false),
        BottariItemUiModel(id = 13, name = "선글라스", isChecked = false),
        BottariItemUiModel(id = 14, name = "모자", isChecked = false),
        BottariItemUiModel(id = 15, name = "슬리퍼", isChecked = false),
        BottariItemUiModel(id = 16, name = "물티슈", isChecked = false),
        BottariItemUiModel(id = 17, name = "우산 또는 우비", isChecked = false),
        BottariItemUiModel(id = 18, name = "읽을 책", isChecked = false),
        BottariItemUiModel(id = 19, name = "손소독제", isChecked = false),
        BottariItemUiModel(id = 20, name = "텀블러", isChecked = false),
    )

val fixtureList3 =
    listOf(
        BottariItemUiModel(id = 21, name = "수영복", isChecked = false),
        BottariItemUiModel(id = 22, name = "선크림", isChecked = false),
        BottariItemUiModel(id = 23, name = "비치타월", isChecked = false),
        BottariItemUiModel(id = 24, name = "방수팩", isChecked = false),
        BottariItemUiModel(id = 25, name = "바람막이", isChecked = false),
        BottariItemUiModel(id = 26, name = "등산화", isChecked = false),
        BottariItemUiModel(id = 27, name = "작은 배낭", isChecked = false),
        BottariItemUiModel(id = 28, name = "간식", isChecked = false),
        BottariItemUiModel(id = 29, name = "셀카봉", isChecked = false),
        BottariItemUiModel(id = 30, name = "여행자 보험증", isChecked = false),
    )
