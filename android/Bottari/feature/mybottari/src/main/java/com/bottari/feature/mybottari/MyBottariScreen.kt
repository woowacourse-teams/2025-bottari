package com.bottari.feature.mybottari

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.common.util.DeeplinkHelper
import com.bottari.core.ui.provider.LocalNetworkManager
import com.bottari.feature.mybottari.component.MyBottariContent
import com.bottari.feature.mybottari.component.MyBottariDialogType
import com.bottari.feature.mybottari.component.MyBottariDialogs

@Composable
fun MyBottariScreen(
    snackbarState: SnackbarHostState,
    onNavigateToPersonalEdit: (Long, Boolean) -> Unit,
    onNavigateToTeamEdit: (Long, Boolean) -> Unit,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
    viewModel: MyBottariViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val networkManager = LocalNetworkManager.current
    val isConnected = networkManager.isConnected.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val clipboard = LocalClipboard.current

    var dialogText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(isConnected) {
        if (isConnected) viewModel.fetchTeamBottaries()
    }

    LaunchedEffect(uiState.showDialogType) {
        if (uiState.showDialogType == MyBottariDialogType.CODE) {
            val clipData = clipboard.nativeClipboard.primaryClip
            clipData?.let { data ->
                if (data.itemCount > 0) {
                    val firstItem = clipData.getItemAt(0)
                    val inviteLink = firstItem.text.toString()
                    dialogText = DeeplinkHelper.getInviteCode(inviteLink) ?: ""
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                MyBottariUiEvent.DeletePersonalBottariFailure -> {
                    snackbarState.showSnackbar(context.getString(R.string.bottari_home_delete_failure_text))
                }

                MyBottariUiEvent.DeletePersonalBottariSuccess -> {
                    snackbarState.showSnackbar(context.getString(R.string.bottari_home_delete_success_text))
                }

                MyBottariUiEvent.ExitTeamBottariFailure -> {
                    snackbarState.showSnackbar(context.getString(R.string.exit_team_bottari_failure_text))
                }

                MyBottariUiEvent.ExitTeamBottariSuccess -> {
                    snackbarState.showSnackbar(context.getString(R.string.exit_team_bottari_success_text))
                }

                MyBottariUiEvent.FetchBottariFailure -> {
                    snackbarState.showSnackbar(context.getString(R.string.bottari_home_fetch_failure_text))
                }

                MyBottariUiEvent.JoinTeamBottariFailure -> {
                    snackbarState.showSnackbar(context.getString(R.string.join_team_bottari_failure_text))
                }

                MyBottariUiEvent.CreateBottariFailure -> {
                    snackbarState.showSnackbar(context.getString(R.string.bottari_create_failure_text))
                }

                is MyBottariUiEvent.CreatePersonalBottariSuccess -> {
                    onNavigateToPersonalEdit(uiEvent.bottariId, true)
                }

                is MyBottariUiEvent.CreateTeamBottariSuccess -> {
                    onNavigateToTeamEdit(uiEvent.bottariId, true)
                }
            }
        }
    }

    val defaultBottariTitle = stringResource(id = R.string.bottari_create_default_title_text)

    uiState.showDialogType?.let { type ->
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
        uiState = uiState,
        isConnected = isConnected,
        onRetryClick = viewModel::fetchTeamBottaries,
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
