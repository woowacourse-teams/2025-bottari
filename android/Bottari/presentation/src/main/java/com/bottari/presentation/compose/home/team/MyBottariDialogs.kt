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
    viewModel: MyBottariViewModel
) {
    var bottariTitle by remember { mutableStateOf("") }

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
                    viewModel.createPersonalBottari(bottariTitle)
                },
                isClickable = bottariTitle.isNotBlank(),
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
                    viewModel.createTeamBottari(bottariTitle)
                },
                isClickable = bottariTitle.isNotBlank(),
            )
        }
    }
}
