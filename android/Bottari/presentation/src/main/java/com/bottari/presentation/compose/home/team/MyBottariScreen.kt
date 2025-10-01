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
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    var dialogText by remember { mutableStateOf("") }

    val deleteFailureMsg = stringResource(id = R.string.bottari_home_delete_failure_text)
    val deleteSuccessMsg = stringResource(id = R.string.bottari_home_delete_success_text)
    val exitFailureMsg = stringResource(id = R.string.exit_team_bottari_failure_text)
    val exitSuccessMsg = stringResource(id = R.string.exit_team_bottari_success_text)
    val fetchFailureMsg = stringResource(id = R.string.bottari_home_fetch_failure_text)
    val joinFailureMsg = stringResource(id = R.string.join_team_bottari_failure_text)
    val createFailureMsg = stringResource(id = R.string.bottari_create_failure_text)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                MyBottariUiEvent.DeletePersonalBottariFailure ->
                    snackbarState.showSnackbar(deleteFailureMsg)

                MyBottariUiEvent.DeletePersonalBottariSuccess ->
                    snackbarState.showSnackbar(deleteSuccessMsg)

                MyBottariUiEvent.ExitTeamBottariFailure ->
                    snackbarState.showSnackbar(exitFailureMsg)

                MyBottariUiEvent.ExitTeamBottariSuccess ->
                    snackbarState.showSnackbar(exitSuccessMsg)

                MyBottariUiEvent.FetchTeamBottariFailure,
                MyBottariUiEvent.FetchPersonalBottariFailure,
                ->
                    snackbarState.showSnackbar(fetchFailureMsg)

                MyBottariUiEvent.JoinTeamBottariFailure ->
                    snackbarState.showSnackbar(joinFailureMsg)

                MyBottariUiEvent.CreatePersonalBottariFailure,
                MyBottariUiEvent.CreateTeamBottariFailure,
                ->
                    snackbarState.showSnackbar(createFailureMsg)

                is MyBottariUiEvent.CreatePersonalBottariSuccess ->
                    onNavigateToPersonalEdit(event.bottariId, true)

                is MyBottariUiEvent.CreateTeamBottariSuccess ->
                    onNavigateToTeamEdit(event.bottariId, true)
            }
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
        onClickPersonalBottari = onNavigateToPersonalChecklist,
        onClickTeamBottari = onNavigateToTeamChecklist,
        onDeletePersonalBottari = viewModel::deletePersonalBottari,
        onDeleteTeamBottari = viewModel::deleteTeamBottari,
        onEditPersonalBottari = { bottariId -> onNavigateToPersonalEdit(bottariId, false) },
        onEditTeamBottari = { bottariId -> onNavigateToTeamEdit(bottariId, false) },
        onOpenPersonalDialog = { viewModel.openDialog(MyBottariDialogType.PERSONAL) },
        onOpenTeamDialog = { viewModel.openDialog(MyBottariDialogType.TEAM) },
        onOpenCodeDialog = { viewModel.openDialog(MyBottariDialogType.CODE) },
    )
}
