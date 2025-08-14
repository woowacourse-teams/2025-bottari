package com.bottari.presentation.view.checklist.team.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.view.checklist.team.checklist.ChecklistType

data class TeamStatusUiState(
    val isLoading: Boolean = false,
    val teamChecklistStatus: TeamChecklistUiModel? = null,
    val item: TeamProductStatusUiModel? = null,
    val teamChecklistItems: List<TeamProductStatusItem> = listOf(),
)

sealed interface TeamStatusUiEvent {
    data object FetchChecklistFailure : TeamStatusUiEvent

    data object CheckItemFailure : TeamStatusUiEvent
}

class TeamStatusViewModel(
    private val stateHandle: SavedStateHandle,
) : BaseViewModel<TeamStatusUiState, TeamStatusUiEvent>(TeamStatusUiState()) {
    init {
        fetchTeamStatus()
    }

    private fun fetchTeamStatus() {
        updateState { copy(teamChecklistStatus = create(), item = create().sharedItems[0]) }
        generateTeamItemsList(create())
    }

    fun selectItem(item: TeamProductStatusUiModel) {
        updateState { copy(item = item) }
    }

    private fun generateTeamItemsList(teamChecklistUiModel: TeamChecklistUiModel) {
        val newItems =
            buildList<TeamProductStatusItem> {
                add(TeamChecklistTypeUiModel(ChecklistType.SHARED))
                addAll(teamChecklistUiModel.sharedItems)
                add(TeamChecklistTypeUiModel(ChecklistType.ASSIGNED))
                addAll(teamChecklistUiModel.assignedItems)
            }

        updateState { copy(teamChecklistItems = newItems) }
    }

    fun create(): TeamChecklistUiModel {
        val sharedItems =
            listOf(
                TeamProductStatusUiModel(
                    name = "공용 멀티탭",
                    checkItemsCount = 5,
                    totalItemsCount = 5,
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "철수", checked = true),
                            MemberCheckStatusUiModel(name = "영희", checked = true),
                            MemberCheckStatusUiModel(name = "민준", checked = true),
                            MemberCheckStatusUiModel(name = "서연", checked = true),
                            MemberCheckStatusUiModel(name = "지훈", checked = true),
                        ),
                ),
                TeamProductStatusUiModel(
                    name = "구급상자",
                    checkItemsCount = 3,
                    totalItemsCount = 5,
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "철수", checked = true),
                            MemberCheckStatusUiModel(name = "영희", checked = false),
                            MemberCheckStatusUiModel(name = "민준", checked = true),
                            MemberCheckStatusUiModel(name = "서연", checked = true),
                            MemberCheckStatusUiModel(name = "지훈", checked = false),
                        ),
                ),
                TeamProductStatusUiModel(
                    name = "보드게임",
                    checkItemsCount = 0,
                    totalItemsCount = 5,
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "철수", checked = false),
                            MemberCheckStatusUiModel(name = "영희", checked = false),
                            MemberCheckStatusUiModel(name = "민준", checked = false),
                            MemberCheckStatusUiModel(name = "서연", checked = false),
                            MemberCheckStatusUiModel(name = "지훈", checked = false),
                        ),
                ),
            )

        // 할당 아이템 목록 더미 데이터
        val assignedItems =
            listOf(
                TeamProductStatusUiModel(
                    name = "항공권 출력",
                    checkItemsCount = 2,
                    totalItemsCount = 2,
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "철수", checked = true),
                            MemberCheckStatusUiModel(name = "영희", checked = true),
                        ),
                ),
                TeamProductStatusUiModel(
                    name = "렌터카 예약",
                    checkItemsCount = 1,
                    totalItemsCount = 2,
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "서연", checked = true),
                            MemberCheckStatusUiModel(name = "지훈", checked = false),
                        ),
                ),
                TeamProductStatusUiModel(
                    name = "맛집 리스트업",
                    checkItemsCount = 0,
                    totalItemsCount = 2,
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "영희", checked = false),
                            MemberCheckStatusUiModel(name = "지훈", checked = false),
                        ),
                ),
            )

        return TeamChecklistUiModel(
            sharedItems = sharedItems,
            assignedItems = assignedItems,
        )
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamStatusViewModel(
                        stateHandle,
                    )
                }
            }
    }
}
