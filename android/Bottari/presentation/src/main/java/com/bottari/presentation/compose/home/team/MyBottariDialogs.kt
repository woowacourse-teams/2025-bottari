package com.bottari.presentation.compose.home.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun MyBottariDialogs(
    uiState: MyBottariUiState,
    viewModel: MyBottariViewModel,
) {
    var bottariTitle by remember { mutableStateOf("") }
    var bottariCode by remember { mutableStateOf("") }

    if (uiState.showCodeDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                viewModel.closeCodeDialog()
                bottariCode = ""
            },
        ) {
            TeamBottariJoinDialog(
                text = bottariCode,
                onChangeText = { newText -> bottariCode = newText },
                onClick = {
                    viewModel.inputTeamBottariCode(bottariCode)
                },
                isClickable = true,
            )
        }
    }

    if (uiState.showPersonalDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                viewModel.closePersonalDialog()
                bottariTitle = ""
            },
        ) {
            BottariCreateDialog(
                text = bottariTitle,
                onChangeText = { newText -> bottariTitle = newText },
                onClick = {
                    viewModel.createPersonalBottari(bottariTitle.ifBlank { "새 보따리" })
                },
                isClickable = true,
                placeholder = "새 보따리",
            )
        }
    }
    if (uiState.showTeamDialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                viewModel.closeTeamDialog()
                bottariTitle = ""
            },
        ) {
            BottariCreateDialog(
                text = bottariTitle,
                onChangeText = { newText -> bottariTitle = newText },
                onClick = {
                    viewModel.createTeamBottari(bottariTitle.ifBlank { "새 보따리" })
                },
                isClickable = true,
                placeholder = "새 보따리",
            )
        }
    }
}
