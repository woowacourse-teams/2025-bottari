package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.model.bottari.MyBottariUiModel

@Composable
fun MyBottariContent(
    uiState: MyBottariUiState,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val pageTitles = listOf("공통", "개인", "팀")

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
            when (page) {
                0 ->
                    BottariList(
                        bottaries = uiState.myBottaries,
                        onBottariClick = onBottariClick,
                    )
                1 ->
                    BottariList(
                        bottaries = uiState.personalBottaries,
                        onBottariClick = onBottariClick,
                    )
                2 ->
                    BottariList(
                        bottaries = uiState.teamBottaries,
                        onBottariClick = onBottariClick,
                    )
            }
        }
    }
}
