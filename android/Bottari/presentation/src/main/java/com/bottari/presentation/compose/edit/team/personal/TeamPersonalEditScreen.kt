package com.bottari.presentation.compose.edit.team.personal

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.edit.team.component.TeamChecklistEditScreen

@Composable
fun TeamPersonalScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamPersonalEditViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                TeamPersonalEditUiEvent.CreateItemFailureCompose ->
                    snackbarHostState.showSnackbar("물건 생성하기에 실패했어요")

                TeamPersonalEditUiEvent.DeleteItemFailureCompose ->
                    snackbarHostState.showSnackbar("물건 삭제하기에 실패했어요")

                TeamPersonalEditUiEvent.FetchTeamPersonalItemsFailure ->
                    snackbarHostState.showSnackbar("물건 불러오기에 실패했어요")
            }
        }
    }

    TeamChecklistEditScreen(
        items = uiState.personalItems,
        isInvalidItem = uiState.isAlreadyExist,
        isSavable = uiState.isSavable,
        onDeleteItem = viewModel::deleteItem,
        onSaveItem = viewModel::createItem,
        newItemName = uiState.inputText,
        onChangeItemName = viewModel::updateInput,
        modifier = modifier,
    )
}
