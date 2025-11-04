package com.bottari.presentation.compose.edit.team.personal

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.edit.team.main.TeamChecklistEditScreen

@Composable
fun TeamPersonalScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamPersonalItemEditViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->

            when (uiEvent) {
                TeamPersonalItemEditEvent.CreateItemSuccessCompose -> return@collect
                TeamPersonalItemEditEvent.CreateItemFailureCompose -> snackbarHostState.showSnackbar(
                    "물품 생성하기에 실패했습니다"
                )

                TeamPersonalItemEditEvent.DeleteItemFailureCompose -> snackbarHostState.showSnackbar(
                    "물품 삭제하기에 실패했습니다"
                )

                TeamPersonalItemEditEvent.FetchTeamPersonalItemsFailure -> snackbarHostState.showSnackbar(
                    "물품 불러오기에 실패했습니다"
                )
            }
        }
    }
    TeamChecklistEditScreen(
        items = uiState.personalItems,
        isInvalidItem = uiState.isAlreadyExist,
        isSavable = (uiState.inputText.isNotBlank() && !uiState.isAlreadyExist),
        onDeleteItem = viewModel::deleteItem,
        onSaveItem = viewModel::createItem,
        newItemName = uiState.inputText,
        onChangeItemName = { input -> viewModel.updateInput(input) },
        modifier = modifier,
    )
}
