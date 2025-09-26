package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    onNavigateToTeamChecklist: (Long, String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        floatingActionButton = {
            AddBottariButton(
                80.dp,
                { viewModel.openTeamDialog() },
                { viewModel.openPersonalDialog() },
            )
        },
    ) { innerPadding ->
        MyBottariContent(
            modifier = Modifier,
            uiState = uiState,
            onNavigateToPersonalChecklist = onNavigateToPersonalChecklist,
            onNavigateToTeamChecklist = onNavigateToTeamChecklist
        )
    }
}
