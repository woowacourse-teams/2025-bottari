package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel

@Composable
fun MyBottariContent(
    uiState: MyBottariUiState,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
    onDeletePersonalBottari: (Long) -> Unit,
    onDeleteTeamBottari: (Long) -> Unit,
    onEditPersonalBottari: (Long, Boolean) -> Unit,
    onEditTeamBottari: (Long, Boolean) -> Unit,
    onOpenPersonalDialog: () -> Unit,
    onOpenTeamDialog: () -> Unit,
    onOpenCodeDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCreateBottariBtnExpanded by remember { mutableStateOf(false) }

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
        if (isCreateBottariBtnExpanded) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            isCreateBottariBtnExpanded = false
                        },
            )
        }
        AddBottariButton(
            buttonSize = 80.dp,
            onCodeClick = onOpenCodeDialog,
            onTeamClick = onOpenTeamDialog,
            onPersonalClick = onOpenPersonalDialog,
            isExpanded = isCreateBottariBtnExpanded,
            onClick = { isCreateBottariBtnExpanded = !isCreateBottariBtnExpanded },
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyBottariContentPreview() {
    val fakeUiState =
        MyBottariUiState(
            personalBottaries =
                listOf(
                    BottariUiModel(
                        id = 1,
                        title = "개인 보따리 1",
                        totalQuantity = 10,
                        checkedQuantity = 5,
                        alarm = null,
                    ),
                    BottariUiModel(
                        id = 2,
                        title = "마트 장보기",
                        totalQuantity = 5,
                        checkedQuantity = 1,
                        alarm = null,
                    ),
                ),
            teamBottaries =
                listOf(
                    TeamBottariUiModel(
                        id = 3,
                        title = "팀 프로젝트 준비물",
                        totalQuantity = 8,
                        checkedQuantity = 8,
                        memberCount = 5,
                        alarm = null,
                    ),
                    TeamBottariUiModel(
                        id = 4,
                        title = "가족 여행",
                        totalQuantity = 20,
                        checkedQuantity = 10,
                        memberCount = 4,
                        alarm = null,
                    ),
                ),
        )

    BottariTheme {
        MyBottariContent(
            uiState = fakeUiState,
            onOpenPersonalDialog = {},
            onOpenTeamDialog = {},
            onOpenCodeDialog = {},
            onNavigateToPersonalChecklist = { _, _ -> },
            onNavigateToTeamChecklist = { _, _ -> },
            onDeletePersonalBottari = {},
            onDeleteTeamBottari = {},
            onEditPersonalBottari = { _, _ -> },
            onEditTeamBottari = { _, _ -> },
        )
    }
}
