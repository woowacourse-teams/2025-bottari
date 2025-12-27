package com.bottari.presentation.compose.edit.personal.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.IndeterminateCircularIndicator
import com.bottari.core.ui.extension.noRippleClickable
import com.bottari.presentation.R
import com.bottari.core.ui.R as UIR
import com.bottari.presentation.compose.edit.personal.item.component.ItemEditEmptyView
import com.bottari.presentation.compose.edit.personal.item.component.ItemEditInputBar
import com.bottari.presentation.compose.edit.personal.item.component.ItemEditLazyColumn
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

@Composable
fun PersonalItemEditScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: PersonalItemEditViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                PersonalItemEditUiEvent.DeleteItemFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(
                            R.string.bottari_personal_item_delete_failure_text,
                        ),
                    )
                }

                PersonalItemEditUiEvent.FetchBottariItemsFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.bottari_personal_item_fetch_failure_text),
                    )
                }

                PersonalItemEditUiEvent.SaveBottariItemFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(
                            UIR.string.common_save_failure_text,
                        ),
                    )
                }
            }
        }
    }

    ItemEditContent(
        state = uiState.value,
        listState = listState,
        onNameChange = viewModel::updateItemName,
        onSaveItem = viewModel::saveItem,
        onDeleteItem = viewModel::deleteItem,
        modifier =
            modifier
                .fillMaxSize()
                .noRippleClickable {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
    )
}

@Composable
private fun ItemEditContent(
    state: PersonalItemEditUiState,
    listState: LazyListState,
    onNameChange: (String) -> Unit,
    onSaveItem: () -> Unit,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (state.isLoading) {
            IndeterminateCircularIndicator()
            return@Column
        }

        ItemEditBody(
            isEmpty = state.isEmpty,
            items = state.items,
            listState = listState,
            onDeleteClick = onDeleteItem,
            modifier = Modifier.weight(1f),
        )

        ItemEditInputBar(
            itemName = state.itemName,
            isInvalidItem = state.isInvalidItem,
            isSavable = state.isSavable,
            onNameChange = onNameChange,
            onSaveItem = onSaveItem,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        )
    }
}

@Composable
private fun ItemEditBody(
    isEmpty: Boolean,
    items: List<PersonalChecklistItemUiModel>,
    listState: LazyListState,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (isEmpty) {
            ItemEditEmptyView(modifier = Modifier.fillMaxSize())
        } else {
            ItemEditLazyColumn(
                items = items,
                onDeleteClick = onDeleteClick,
                listState = listState,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEditContentPreview() {
    BottariTheme {
        ItemEditContent(
            state = PersonalItemEditUiState(),
            listState = rememberLazyListState(),
            onNameChange = {},
            onSaveItem = {},
            onDeleteItem = {},
        )
    }
}
