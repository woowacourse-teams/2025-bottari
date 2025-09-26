package com.bottari.presentation.compose.home.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyBottariScreen(
    onNavigateToPersonalEdit: (Long, Boolean) -> Unit,
    onNavigateToTeamEdit: (Long, Boolean) -> Unit,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
) {
    val viewModel: MyBottariViewModel =
        viewModel(
            factory = MyBottariViewModel.Factory(),
        )

    val uiState: MyBottariUiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                MyBottariUiEvent.PersonalBottariFetchFailure -> TODO()
                MyBottariUiEvent.TeamBottariFetchFailure -> TODO()
                is MyBottariUiEvent.CreatePersonalBottariSuccess ->
                    onNavigateToPersonalEdit(
                        event.bottariId,
                        true,
                    )

                is MyBottariUiEvent.CreateTeamBottariSuccess ->
                    onNavigateToTeamEdit(
                        event.bottariId,
                        true,
                    )
            }
        }
    }

    MyBottariDialogs(
        uiState = uiState,
        viewModel = viewModel,
    )

    MyBottariScaffold(
        viewModel = viewModel,
        uiState = uiState,
        onNavigateToPersonalChecklist = onNavigateToPersonalChecklist,
        onNavigateToTeamChecklist = onNavigateToTeamChecklist,
    )
}

@Composable
fun PopUpMenu() {
}
