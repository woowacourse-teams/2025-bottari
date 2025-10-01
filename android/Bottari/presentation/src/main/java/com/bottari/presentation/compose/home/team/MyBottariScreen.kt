package com.bottari.presentation.compose.home.team

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R

@Composable
fun MyBottariScreen(
    onNavigateToPersonalEdit: (Long, Boolean) -> Unit,
    onNavigateToTeamEdit: (Long, Boolean) -> Unit,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
    snackbarState: SnackbarHostState,
    viewModel: MyBottariViewModel =
        viewModel(
            factory = MyBottariViewModel.Factory(),
        ),
) {
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    var dialogText by remember { mutableStateOf("") }

    val snackbarMessage =
        when (uiEvent.value) {
            MyBottariUiEvent.DeletePersonalBottariFailure -> stringResource(id = R.string.bottari_home_delete_failure_text)
            MyBottariUiEvent.DeletePersonalBottariSuccess -> stringResource(id = R.string.bottari_home_delete_success_text)
            MyBottariUiEvent.ExitTeamBottariFailure -> stringResource(id = R.string.exit_team_bottari_failure_text)
            MyBottariUiEvent.ExitTeamBottariSuccess -> stringResource(id = R.string.exit_team_bottari_success_text)
            MyBottariUiEvent.FetchTeamBottariFailure,
            MyBottariUiEvent.FetchPersonalBottariFailure,
            -> stringResource(id = R.string.bottari_home_fetch_failure_text)

            MyBottariUiEvent.JoinTeamBottariFailure -> stringResource(R.string.join_team_bottari_failure_text)
            MyBottariUiEvent.CreatePersonalBottariFailure, MyBottariUiEvent.CreateTeamBottariFailure ->
                stringResource(
                    R.string.bottari_create_failure_text,
                )

            else -> null
        }

    LaunchedEffect(uiEvent.value) {
        snackbarMessage?.let {
            snackbarState.showSnackbar(it)
        }

        when (val event = uiEvent.value) {
            is MyBottariUiEvent.CreatePersonalBottariSuccess ->
                onNavigateToPersonalEdit(event.bottariId, true)

            is MyBottariUiEvent.CreateTeamBottariSuccess ->
                onNavigateToTeamEdit(event.bottariId, true)

            else -> {}
        }
    }

    val defaultBottariTitle = stringResource(id = R.string.bottari_create_default_title_text)

    uiState.value.showDialogType.let { type ->
        MyBottariDialogs(
            dialogType = type,
            onChangeText = { newText -> dialogText = newText },
            onClick = {
                viewModel.onClickDialog(dialogText.ifBlank { defaultBottariTitle })
                dialogText = ""
            },
            onDismiss = {
                viewModel.closeDialog()
                dialogText = ""
            },
            text = dialogText,
            defaultBottariTitle = defaultBottariTitle,
        )
    }

    MyBottariContent(
        uiState = uiState.value,
        modifier = Modifier,
        onNavigateToPersonalChecklist = onNavigateToPersonalChecklist,
        onNavigateToTeamChecklist = onNavigateToTeamChecklist,
        onDeletePersonalBottari = { viewModel.deletePersonalBottari(it) },
        onDeleteTeamBottari = { viewModel.deleteTeamBottari(it) },
        onEditPersonalBottari = onNavigateToPersonalEdit,
        onEditTeamBottari = onNavigateToTeamEdit,
        onOpenPersonalDialog = { viewModel.openDialog(MyBottariDialogType.PERSONAL) },
        onOpenTeamDialog = { viewModel.openDialog(MyBottariDialogType.TEAM) },
        onOpenCodeDialog = { viewModel.openDialog(MyBottariDialogType.CODE) },
    )
}
