package com.bottari.presentation.compose.edit.team.personal

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.edit.team.main.ComposeTeamChecklistEditScreen

@Composable
fun ComposeTeamPersonalScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamPersonalItemEditViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(null)

    LaunchedEffect(uiEvent) {
        when (uiEvent ?: return@LaunchedEffect) {
            ComposeTeamPersonalItemEditEvent.CreateItemSuccessCompose -> return@LaunchedEffect
                ComposeTeamPersonalItemEditEvent.CreateItemFailureCompose -> snackbarHostState.showSnackbar("물품 생성하기에 실패했습니다")
                ComposeTeamPersonalItemEditEvent.DeleteItemFailureCompose -> snackbarHostState.showSnackbar("물품 삭제하기에 실패했습니다")
                ComposeTeamPersonalItemEditEvent.FetchComposeTeamPersonalItemsFailure -> snackbarHostState.showSnackbar("물품 불러오기에 실패했습니다")
            }

        }
        ComposeTeamChecklistEditScreen(
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
