package com.bottari.presentation.compose.home.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun MyBottariDialogs(
    viewModel: MyBottariViewModel,
    defaultBottariTitle: String = "새 보따리",
) {
    var bottariTitle by remember { mutableStateOf("") }
    var bottariCode by remember { mutableStateOf("") }

    if (viewModel.uiState
            .collectAsState()
            .value.showCodeDialog
    ) {
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

    if (viewModel.uiState
            .collectAsState()
            .value.showPersonalDialog
    ) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                viewModel.closePersonalDialog()
                bottariTitle = ""
            },
        ) {
            BottariTheme {
                BottariCreateDialog(
                    text = bottariTitle,
                    onChangeText = { newText -> bottariTitle = newText },
                    onClick = {
                        viewModel.createPersonalBottari(bottariTitle.ifBlank { defaultBottariTitle })
                    },
                    placeholder = defaultBottariTitle,
                )
            }
        }
    }

    if (viewModel.uiState
            .collectAsState()
            .value.showTeamDialog
    ) {
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
                    viewModel.createTeamBottari(bottariTitle.ifBlank { defaultBottariTitle })
                },
                placeholder = defaultBottariTitle,
            )
        }
    }
}
