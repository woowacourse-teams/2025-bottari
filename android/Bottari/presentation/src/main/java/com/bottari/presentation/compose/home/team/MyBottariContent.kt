package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.model.bottari.MyBottariUiModel

@Composable
fun MyBottariContent(
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
    onDeletePersonalBottari: (Long) -> Unit,
    onDeleteTeamBottari: (Long) -> Unit,
    onEditPersonalBottari: (Long, Boolean) -> Unit,
    onEditTeamBottari: (Long, Boolean) -> Unit,
    viewModel: MyBottariViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val pageTitles =
                listOf(
                    stringResource(R.string.common_bottari_text),
                    stringResource(R.string.personal_bottari_text),
                    stringResource(R.string.team_bottari_text),
                )

            BottariTabBar(
                pageTitles = pageTitles,
                pagerState = rememberPagerState(initialPage = 0) { pageTitles.size },
            ) { page ->
                val onBottariClick = { bottari: MyBottariUiModel ->
                    navigateToChecklist(
                        bottari,
                        onNavigateToPersonalChecklist,
                        onNavigateToTeamChecklist,
                    )
                }

                val currentList =
                    when (page) {
                        0 -> uiState.myBottaries
                        1 -> uiState.personalBottaries
                        2 -> uiState.teamBottaries
                        else -> emptyList()
                    }

                BottariList(
                    bottaries = currentList,
                    onBottariClick = onBottariClick,
                    onDeletePersonalBottari = onDeletePersonalBottari,
                    onDeleteTeamBottari = onDeleteTeamBottari,
                    onPersonalBottariEdit = onEditPersonalBottari,
                    onTeamBottariEdit = onEditTeamBottari,
                )
            }
        }
        AddBottariButton(
            buttonSize = 80.dp,
            onCodeClick = { viewModel.openCodeDialog() },
            onTeamClick = { viewModel.openTeamDialog() },
            onPersonalClick = { viewModel.openPersonalDialog() },
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}
