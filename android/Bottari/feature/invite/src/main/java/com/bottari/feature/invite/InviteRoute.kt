package com.bottari.feature.invite

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.core.ui.R
import com.bottari.core.ui.extension.showSnackbar

@Composable
fun InviteRoute(
    snackbarState: SnackbarHostState,
    inviteCode: String,
    onFinished: () -> Unit,
    viewModel: InviteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(inviteCode) {
        if (inviteCode.isBlank()) {
            onFinished()
            return@LaunchedEffect
        }
        viewModel.joinTeamBottari(inviteCode)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                InviteUiEvent.JoinTeamBottariFailure -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = context.getString(R.string.join_team_bottari_failure_text),
                        dismissAction = { onFinished() },
                    )
                }

                InviteUiEvent.JoinTeamBottariSuccess -> {
                    showSnackbar(
                        snackbarState = snackbarState,
                        message = context.getString(R.string.join_team_bottari_success_text),
                        dismissAction = { onFinished() },
                    )
                }
            }
        }
    }

    if (uiState.isLoading) BottariCircularLoader()
}
