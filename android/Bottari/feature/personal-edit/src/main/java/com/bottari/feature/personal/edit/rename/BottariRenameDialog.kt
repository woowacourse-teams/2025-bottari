package com.bottari.feature.personal.edit.rename

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTextField
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.R as UIR

@Composable
fun BottariRenameDialog(
    bottariId: Long,
    bottariTitle: String,
    onDismissRequest: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: BottariRenameViewModel =
        hiltViewModel<BottariRenameViewModel, BottariRenameViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(bottariTitle) {
        viewModel.setInitialTitle(bottariTitle)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                BottariRenameUiEvent.SaveBottariTitleFailure -> {
                    snackbarHostState.showSnackbar(
                        context.getString(R.string.bottari_rename_failure_text),
                    )
                }

                BottariRenameUiEvent.SaveBottariTitleSuccess -> {
                    onDismissRequest()
                }
            }
        }
    }

    BottariRenameDialog(
        bottariTitle = uiState.value.title,
        onDismissRequest = onDismissRequest,
        onTitleChange = viewModel::cacheTitleInput,
        onTitleSave = viewModel::saveBottariTitle,
        isSavable = uiState.value.isSaveEnabled,
        modifier = modifier,
    )
}

@Composable
private fun BottariRenameDialog(
    bottariTitle: String,
    onDismissRequest: () -> Unit,
    onTitleChange: (String) -> Unit,
    onTitleSave: () -> Unit,
    isSavable: Boolean,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = true),
    ) {
        BottariCard(
            modifier = modifier,
            contentPadding = PaddingValues(0.dp),
        ) {
            Column {
                BottariRenameDialogHeader(
                    onDismissRequest = onDismissRequest,
                    modifier = Modifier.fillMaxWidth(),
                )
                Column(
                    modifier =
                        Modifier.padding(
                            horizontal = BottariTheme.spacing.spaceLarge,
                        ),
                ) {
                    BottariTextField(
                        value = bottariTitle,
                        onValueChange = onTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    BottariButton(
                        text = stringResource(R.string.bottari_rename_dialog_btn_text),
                        onClick = onTitleSave,
                        enabled = isSavable,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = BottariTheme.spacing.spaceMedium),
                    )
                }
            }
        }
    }
}

@Composable
private fun BottariRenameDialogHeader(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.bottari_rename_dialog_title_text),
            modifier =
                Modifier.padding(
                    vertical = BottariTheme.spacing.spaceMedium,
                    horizontal = BottariTheme.spacing.spaceLarge,
                ),
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
        BottariIconButton(onClick = onDismissRequest) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(UIR.string.common_close_btn_description),
            )
        }
    }
}

@Preview
@Composable
private fun BottariRenameDialogPreview() {
    var title by remember { mutableStateOf("보따리") }

    BottariTheme {
        BottariRenameDialog(
            bottariTitle = title,
            onDismissRequest = {},
            onTitleChange = { title = it },
            onTitleSave = {},
            isSavable = true,
        )
    }
}
