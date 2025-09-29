package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyBottariScaffold(
    viewModel: MyBottariViewModel,
    uiState: MyBottariUiState,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
    onDeletePersonalBottari: (Long) -> Unit,
    onDeleteTeamBottari: (Long) -> Unit,
    onEditPersonalBottari: (Long, Boolean) -> Unit = { _, _ -> },
    onEditTeamBottari: (Long, Boolean) -> Unit = { _, _ -> },
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        floatingActionButton = {
            AddBottariButton(
                80.dp,
                { viewModel.openCodeDialog() },
                { viewModel.openTeamDialog() },
                { viewModel.openPersonalDialog() },
            )
        },
    ) { innerPadding ->
        MyBottariContent(
            modifier = Modifier,
            uiState = uiState,
            onNavigateToPersonalChecklist = onNavigateToPersonalChecklist,
            onNavigateToTeamChecklist = onNavigateToTeamChecklist,
            onDeletePersonalBottari = onDeletePersonalBottari,
            onDeleteTeamBottari = onDeleteTeamBottari,
            onEditPersonalBottari = onEditPersonalBottari,
            onEditTeamBottari = onEditTeamBottari,
        )
    }
}
