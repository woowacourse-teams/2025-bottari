package com.bottari.feature.team.edit.assigned

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.feature.team.edit.assigned.component.TeamAssignedBottomSheet
import com.bottari.feature.team.edit.assigned.component.TeamAssignedEditItem
import com.bottari.feature.team.edit.component.TeamEditEmptyView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamAssignedScreen(
    bottariId: Long,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamAssignedEditViewModel =
        hiltViewModel<TeamAssignedEditViewModel, TeamAssignedEditViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                TeamAssignedEditUiEvent.CreateItemFailure -> {
                    snackbarHostState.showSnackbar("물건 생성에 실패했어요")
                }

                TeamAssignedEditUiEvent.DeleteItemFailure -> {
                    snackbarHostState.showSnackbar("물건 삭제에 실패했어요")
                }

                TeamAssignedEditUiEvent.FetchTeamAssignedItemsFailure -> {
                    snackbarHostState.showSnackbar("물건 불러오기에 실패했어요")
                }

                TeamAssignedEditUiEvent.SaveItemFailure -> {
                    snackbarHostState.showSnackbar("물건 저장에 실패했어요")
                }
            }
        }
    }

    TeamAssignedScreen(
        uiState = uiState,
        modifier = modifier,
        sheetState = sheetState,
        onChangeInputText = viewModel::updateInput,
        onBottomSheetOpen = viewModel::openBottomSheetState,
        onBottomSheetClose = viewModel::resetState,
        onSaveItem = viewModel::submitItem,
        onEditItem = viewModel::toggleEditState,
        onAllSelect = viewModel::selectAllMember,
        onAllUnSelect = viewModel::unSelectAllMember,
        onSelectMember = viewModel::selectMember,
        onDeleteItem = viewModel::deleteItem,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamAssignedScreen(
    uiState: TeamAssignedEditUiState,
    sheetState: SheetState,
    onChangeInputText: (String) -> Unit,
    onBottomSheetOpen: () -> Unit,
    onBottomSheetClose: () -> Unit,
    onEditItem: (Long) -> Unit,
    onSaveItem: () -> Unit,
    onAllSelect: () -> Unit,
    onAllUnSelect: () -> Unit,
    onDeleteItem: (Long) -> Unit,
    onSelectMember: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        Column(modifier = modifier.fillMaxSize()) {
            if (uiState.assignedItems.isEmpty()) {
                TeamEditEmptyView(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                )
            } else {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    contentPadding = PaddingValues(end = BottariTheme.spacing.space2xSmall),
                    verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
                ) {
                    items(uiState.assignedItems) { item ->
                        val type = item.type as BottariItemTypeUiModel.ASSIGNED
                        TeamAssignedEditItem(
                            item.name,
                            type.members.map { member ->
                                val memberIndex =
                                    uiState.members.indexOfFirst { it.id == member.id }
                                Pair(member.nickname, memberIndex)
                            },
                            onClickEdit = { onEditItem(item.id) },
                            onClickDelete = { onDeleteItem(item.id) },
                        )
                    }
                }
            }
            BottariButton(
                text = "물건 추가",
                onClick = onBottomSheetOpen,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = BottariTheme.spacing.spaceMedium),
            )
            if (uiState.isSheetVisible) {
                TeamAssignedBottomSheet(
                    uiState = uiState,
                    onSaveItem = onSaveItem,
                    onAllSelect = onAllSelect,
                    onBottomSheetClose = onBottomSheetClose,
                    onAllUnSelect = onAllUnSelect,
                    onSelectMember = onSelectMember,
                    onChangeInputText = onChangeInputText,
                    sheetState = sheetState,
                )
            }
        }
        if (uiState.isLoading) BottariCircularLoader()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TeamAssignedScreenPreview() {
    val sheetState = rememberModalBottomSheetState()
    TeamAssignedScreen(
        uiState =
            TeamAssignedEditUiState(
                assignedItems = previewAssignedSelectableItemsLarge,
                members = dummyMembers,
            ),
        sheetState = sheetState,
        onBottomSheetClose = {},
        onEditItem = {},
        onSaveItem = {},
        onAllSelect = {},
        onAllUnSelect = {},
        onSelectMember = {},
        onChangeInputText = {},
        onDeleteItem = {},
        onBottomSheetOpen = {},
    )
}
