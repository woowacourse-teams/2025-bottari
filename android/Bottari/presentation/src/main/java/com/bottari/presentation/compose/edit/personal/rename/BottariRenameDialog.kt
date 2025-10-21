package com.bottari.presentation.compose.edit.personal.rename

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.edit.personal.rename.component.BottariRenameButton
import com.bottari.presentation.compose.edit.personal.rename.component.BottariRenameTextField

@Composable
fun BottariRenameDialog(
    bottariTitle: String,
    onDismissRequest: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: BottariRenameViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setInitialTitle(bottariTitle)
    }

    LaunchedEffect(uiEvent.value) {
        when (uiEvent.value ?: return@LaunchedEffect) {
            BottariRenameUiEvent.SaveBottariTitleFailure ->
                snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.bottari_rename_failure_text,
                    ),
                )

            BottariRenameUiEvent.SaveBottariTitleSuccess -> onDismissRequest()
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
        BottariBox(
            modifier = modifier,
            contentPadding = PaddingValues(0.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.common_close_btn_description),
                        )
                    }
                }
                BottariRenameTextField(
                    title = bottariTitle,
                    onTitleChange = onTitleChange,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = BottariTheme.spacing.spaceLarge),
                )
                BottariRenameButton(
                    onClick = onTitleSave,
                    isClickable = isSavable,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = BottariTheme.spacing.spaceMedium,
                                horizontal = BottariTheme.spacing.spaceLarge,
                            ),
                )
            }
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
