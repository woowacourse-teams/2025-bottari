package com.bottari.presentation.compose.edit.team.assigned

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.edit.team.assigned.component.TeamAssignedBottomSheet
import com.bottari.presentation.compose.edit.team.assigned.component.TeamAssignedEditItem
import com.bottari.presentation.compose.edit.team.component.TeamEditEmptyView
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamAssignedScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamAssignedEditViewModel = viewModel(),
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
            Button(
                onClick = onBottomSheetOpen,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = BottariTheme.spacing.spaceMedium),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = BottariTheme.colors.primary,
                        contentColor = BottariTheme.colors.white,
                        disabledContainerColor = BottariTheme.colors.primary,
                        disabledContentColor = BottariTheme.colors.white,
                    ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceMedium),
            ) { Text(text = "물건 추가", style = BottariTheme.typography.semiBold24.toTextStyle()) }
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
        if (uiState.isLoading) IndeterminateCircularIndicator()
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
