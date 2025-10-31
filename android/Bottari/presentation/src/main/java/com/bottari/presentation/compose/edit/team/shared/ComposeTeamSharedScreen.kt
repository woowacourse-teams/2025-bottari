package com.bottari.presentation.compose.edit.team.shared

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.compose.edit.team.main.ComposeTeamChecklistEditScreen
import com.bottari.presentation.model.bottari.BottariItemUiModel
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

@Composable
fun ComposeTeamSharedScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamSharedItemEditViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(null)

    ComposeTeamChecklistEditScreen(
        items = uiState.sharedItems,
        isInvalidItem = uiState.isAlreadyExist,
        isSavable = (uiState.inputText.isNotBlank() && !uiState.isAlreadyExist),
        onDeleteItem = viewModel::deleteItem,
        onSaveItem = viewModel::createItem,
        newItemName = uiState.inputText,
        onChangeItemName = { input -> viewModel.updateInput(input) },
        modifier = modifier,
    )

    LaunchedEffect(uiEvent) {
        when (uiEvent ?: return@LaunchedEffect) {
            ComposeTeamSharedItemEditEvent.CreateItemSuccussCompose -> return@LaunchedEffect
            ComposeTeamSharedItemEditEvent.CreateItemFailureCompose -> snackbarHostState.showSnackbar("물품 생성에 실패했습니다")
            ComposeTeamSharedItemEditEvent.DeleteItemFailureCompose -> snackbarHostState.showSnackbar("물품 삭제에 실패했습니다")
            ComposeTeamSharedItemEditEvent.FetchComposeTeamSharedItemsFailure -> snackbarHostState.showSnackbar("물품 불러오기에 실패했습니다")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ComposeTeamChecklistEditScreenPreview() {
    val items =
        listOf(
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
        )

    ComposeTeamChecklistEditScreen(
        items = items,
        onDeleteItem = {},
        newItemName = "",
        isInvalidItem = false,
        isSavable = true,
        onChangeItemName = {},
        onSaveItem = {},
    )
}
