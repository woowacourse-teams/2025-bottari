package com.bottari.presentation.compose.edit.team.assigned

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.edit.team.assigned.component.TeamAssignedBottomSheet
import com.bottari.presentation.compose.edit.team.assigned.component.TeamAssignedEditItem
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamAssignedScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamAssignedItemEditViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(null)

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(uiEvent) {
        when (uiEvent ?: return@LaunchedEffect) {
            TeamAssignedItemEditUiEvent.CreateItemFailureUi -> snackbarHostState.showSnackbar("물품 생성에 실패했습니다")
            TeamAssignedItemEditUiEvent.DeleteItemFailureUi -> snackbarHostState.showSnackbar("물품 삭제에 실패했습니다")
            TeamAssignedItemEditUiEvent.FetchTeamAssignedItemsFailureUi -> snackbarHostState.showSnackbar("물품 불러오기에 실패했습니다")
            TeamAssignedItemEditUiEvent.SaveItemFailureUi -> snackbarHostState.showSnackbar("물품 저장에 실패했습니다")
            TeamAssignedItemEditUiEvent.CreateItemSuccessUi,
            TeamAssignedItemEditUiEvent.SaveItemSuccessUi,
            -> return@LaunchedEffect
        }
    }

    TeamAssignedScreen(
        uiState = uiState,
        modifier = modifier,
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        onChangeInputText = { input -> viewModel.updateInput(input) },
        onBottomSheetVisibleChange = { showBottomSheet = !showBottomSheet },
        onBottomSheetClose = {
            showBottomSheet = false
            viewModel.resetState()
        },
        onSaveItem = {
            viewModel.submitItem()
            showBottomSheet = false
            viewModel.refreshAssignedItemsAndMembers()
            viewModel.resetState()
        },
        onEditItem = { itemId ->
            viewModel.toggleEditState(itemId)
            showBottomSheet = true
        },
        onAllSelect = { viewModel.selectAllMember() },
        onAllUnSelect = { viewModel.unSelectAllMember() },
        onSelectMember = { id -> viewModel.selectMember(id) },
        onDeleteItem = { id -> viewModel.deleteItem(id) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamAssignedScreen(
    uiState: TeamAssignedItemEditUiState,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    onChangeInputText: (String) -> Unit,
    onBottomSheetVisibleChange: () -> Unit,
    onBottomSheetClose: () -> Unit,
    onEditItem: (Long) -> Unit,
    onSaveItem: () -> Unit,
    onAllSelect: () -> Unit,
    onAllUnSelect: () -> Unit,
    onDeleteItem: (Long) -> Unit,
    onSelectMember: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
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
                        val memberIndex = uiState.members.indexOfFirst { it.id == member.id }
                        Pair(member.nickname, memberIndex)
                    },
                    onClickEdit = { onEditItem(item.id) },
                    onClickDelete = { onDeleteItem(item.id) },
                )
            }
        }
        Button(
            onClick = { onBottomSheetVisibleChange() },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = BottariTheme.spacing.spaceMedium),
            colors =
                ButtonColors(
                    containerColor = BottariTheme.colors.primary,
                    contentColor = BottariTheme.colors.white,
                    disabledContainerColor = BottariTheme.colors.primary,
                    disabledContentColor = BottariTheme.colors.white,
                ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(vertical = BottariTheme.spacing.spaceMedium),
        ) { Text(text = "물품 추가", style = BottariTheme.typography.semiBold24.toTextStyle()) }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = onBottomSheetClose,
                sheetState = sheetState,
                containerColor = BottariTheme.colors.white,
            ) {
                TeamAssignedBottomSheet(
                    uiState = uiState,
                    onSaveItem = onSaveItem,
                    onAllSelect = onAllSelect,
                    onAllUnSelect = onAllUnSelect,
                    onSelectMember = onSelectMember,
                    onChangeInputText = onChangeInputText,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TeamAssignedScreenPreview() {
    val sheetState = rememberModalBottomSheetState()
    TeamAssignedScreen(
        uiState =
            TeamAssignedItemEditUiState(
                assignedItems = previewAssignedSelectableItemsLarge,
                members = dummyMembers,
            ),
        showBottomSheet = false,
        sheetState = sheetState,
        onBottomSheetVisibleChange = {},
        onBottomSheetClose = {},
        onEditItem = {},
        onSaveItem = {},
        onAllSelect = {},
        onAllUnSelect = {},
        onSelectMember = {},
        onChangeInputText = {},
        onDeleteItem = {},
    )
}
