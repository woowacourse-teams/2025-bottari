package com.bottari.presentation.compose.home.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
        viewModel = viewModel,
    )

    MyBottariContent(
        modifier = Modifier,
        onNavigateToPersonalChecklist = onNavigateToPersonalChecklist,
        onNavigateToTeamChecklist = onNavigateToTeamChecklist,
        onDeletePersonalBottari = { viewModel.deletePersonalBottari(it) },
        onDeleteTeamBottari = { viewModel.deleteTeamBottari(it) },
        onEditPersonalBottari = onNavigateToPersonalEdit,
        onEditTeamBottari = onNavigateToTeamEdit,
        viewModel = viewModel,
    )
}
