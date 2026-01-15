package com.bottari.feature.team.edit.shared

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.feature.team.edit.component.TeamChecklistEditScreen

@Composable
fun TeamSharedScreen(
    bottariId: Long,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: TeamSharedEditViewModel =
        hiltViewModel<TeamSharedEditViewModel, TeamSharedEditViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                TeamSharedEditUiEvent.CreateItemFailure -> snackbarHostState.showSnackbar("물건 생성에 실패했어요")
                TeamSharedEditUiEvent.DeleteItemFailure -> snackbarHostState.showSnackbar("물건 삭제에 실패했어요")
                TeamSharedEditUiEvent.FetchTeamSharedItemsFailure -> snackbarHostState.showSnackbar("물건 불러오기에 실패했어요")
            }
        }
    }

    TeamChecklistEditScreen(
        items = uiState.sharedItems,
        isInvalidItem = uiState.isAlreadyExist,
        isSavable = uiState.isSavable,
        isLoading = uiState.isLoading,
        onDeleteItem = viewModel::deleteItem,
        onSaveItem = viewModel::createItem,
        newItemName = uiState.inputText,
        onChangeItemName = viewModel::updateInput,
        modifier = modifier,
    )
}
