package com.bottari.presentation.compose.edit.personal.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.edit.personal.item.PersonalItemEditUiEvent
import com.bottari.presentation.compose.edit.personal.item.PersonalItemEditViewModel
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

@Composable
fun ItemEditContent(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: PersonalItemEditViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiEvent.value) {
        when (uiEvent.value ?: return@LaunchedEffect) {
            PersonalItemEditUiEvent.DeleteItemFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.bottari_personal_item_delete_failure_text,
                    ),
                )

            PersonalItemEditUiEvent.FetchBottariItemsFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(R.string.bottari_personal_item_fetch_failure_text),
                )

            PersonalItemEditUiEvent.SaveBottariItemFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.common_save_failure_text,
                    ),
                )
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .imePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
    ) {
        if (uiState.value.isLoading) {
            IndeterminateCircularIndicator()
            return@Column
        }

        Box(modifier = Modifier.weight(1f)) {
            if (uiState.value.isEmpty) {
                ItemEditEmptyView(modifier = Modifier.fillMaxSize())
            } else {
                ItemEditLazyColumn(
                    items = uiState.value.items,
                    onDeleteClick = viewModel::deleteItem,
                    listState = listState,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ItemTextField(
                itemName = uiState.value.itemName,
                onNameChange = viewModel::updateItemName,
                onSaveItem = viewModel::saveItem,
                isError = uiState.value.isInvalidateItem,
                modifier =
                    Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start = BottariTheme.spacing.spaceSmall),
            )
            IconButton(
                onClick = viewModel::saveItem,
                enabled = uiState.value.isSavable,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.bottari_btn_item_add_description),
                )
            }
        }
    }
}

@Composable
private fun ItemEditEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bottari_item_empty_view),
            contentDescription = stringResource(R.string.bottari_icon_item_empty_description),
            modifier = Modifier.size(80.dp),
            alpha = 0.25f,
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.bottari_personal_item_empty_view_title),
            style =
                BottariTheme.typography.bold20
                    .copy(color = BottariTheme.colors.gray500)
                    .toTextStyle(),
        )
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = stringResource(R.string.bottari_personal_item_empty_view_text),
            style =
                BottariTheme.typography.medium16
                    .copy(color = BottariTheme.colors.gray500)
                    .toTextStyle(),
        )
    }
}

@Composable
private fun ItemEditContent(
    isLoading: Boolean,
    isEmpty: Boolean,
    items: List<ChecklistItemUiModel>,
    itemName: String,
    onItemNameChange: (String) -> Unit,
    isError: Boolean,
    onSaveClick: () -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                IndeterminateCircularIndicator()
            }
            return@Column
        }

        Box(modifier = Modifier.weight(1f)) {
            if (isEmpty) {
                ItemEditEmptyView(modifier = Modifier.fillMaxSize())
            } else {
                ItemEditLazyColumn(
                    items = items,
                    onDeleteClick = onDeleteClick,
                    listState = rememberLazyListState(),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ItemTextField(
                itemName = itemName,
                onNameChange = onItemNameChange,
                onSaveItem = {},
                isError = isError,
                modifier =
                    Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start = BottariTheme.spacing.spaceSmall),
            )
            IconButton(onClick = onSaveClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.bottari_btn_item_add_description),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEditContentPreview() {
    ItemEditContent(
        isLoading = false,
        isEmpty = false,
        items = listOf(ChecklistItemUiModel(id = 1L, name = "물건", isChecked = false)),
        itemName = "",
        onItemNameChange = {},
        isError = false,
        onSaveClick = {},
        onDeleteClick = {},
    )
}
