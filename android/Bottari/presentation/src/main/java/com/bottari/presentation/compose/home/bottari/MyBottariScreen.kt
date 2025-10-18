package com.bottari.presentation.compose.home.bottari

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R

@Composable
fun MyBottariScreen(
    snackbarState: SnackbarHostState,
    onNavigateToPersonalEdit: (Long, Boolean) -> Unit,
    onNavigateToTeamEdit: (Long, Boolean) -> Unit,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
    viewModel: MyBottariViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(null)

    val context = LocalContext.current

    var dialogText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiEvent.value) {
        when (val event = uiEvent.value ?: return@LaunchedEffect) {
            MyBottariUiEvent.DeletePersonalBottariFailure ->
                snackbarState.showSnackbar(context.getString(R.string.bottari_home_delete_failure_text))

            MyBottariUiEvent.DeletePersonalBottariSuccess ->
                snackbarState.showSnackbar(context.getString(R.string.bottari_home_delete_success_text))

            MyBottariUiEvent.ExitTeamBottariFailure ->
                snackbarState.showSnackbar(context.getString(R.string.exit_team_bottari_failure_text))

            MyBottariUiEvent.ExitTeamBottariSuccess ->
                snackbarState.showSnackbar(context.getString(R.string.exit_team_bottari_success_text))

            MyBottariUiEvent.FetchBottariFailure ->
                snackbarState.showSnackbar(context.getString(R.string.bottari_home_fetch_failure_text))

            MyBottariUiEvent.JoinTeamBottariFailure ->
                snackbarState.showSnackbar(context.getString(R.string.join_team_bottari_failure_text))

            MyBottariUiEvent.CreateBottariFailure ->
                snackbarState.showSnackbar(context.getString(R.string.bottari_create_failure_text))

            is MyBottariUiEvent.CreatePersonalBottariSuccess ->
                onNavigateToPersonalEdit(event.bottariId, true)

            is MyBottariUiEvent.CreateTeamBottariSuccess ->
                onNavigateToTeamEdit(event.bottariId, true)
        }
    }

    val defaultBottariTitle = stringResource(id = R.string.bottari_create_default_title_text)

    uiState.value.showDialogType?.let { type ->
        MyBottariDialogs(
            dialogType = type,
            text = dialogText,
            onChangeText = { newText -> dialogText = newText },
            onClick = {
                viewModel.onClickDialog(dialogText.ifBlank { defaultBottariTitle })
                dialogText = ""
            },
            onDismiss = {
                viewModel.closeDialog()
                dialogText = ""
            },
            defaultBottariTitle = defaultBottariTitle,
        )
    }

    MyBottariContent(
        uiState = uiState.value,
        onClickPersonalBottari = onNavigateToPersonalChecklist,
        onClickTeamBottari = onNavigateToTeamChecklist,
        onDeletePersonalBottari = viewModel::deletePersonalBottari,
        onDeleteTeamBottari = viewModel::deleteTeamBottari,
        onEditPersonalBottari = { bottariId -> onNavigateToPersonalEdit(bottariId, false) },
        onEditTeamBottari = { bottariId -> onNavigateToTeamEdit(bottariId, false) },
        onOpenPersonalDialog = { viewModel.openDialog(MyBottariDialogType.PERSONAL) },
        onOpenTeamDialog = { viewModel.openDialog(MyBottariDialogType.TEAM) },
        onOpenCodeDialog = { viewModel.openDialog(MyBottariDialogType.CODE) },
        modifier = Modifier,
    )
}
