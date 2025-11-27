package com.bottari.presentation.compose.home.bottari.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariTabBar
import com.bottari.presentation.compose.common.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.home.bottari.MyBottariUiState
import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel
import kotlinx.coroutines.delay

@Composable
fun MyBottariContent(
    uiState: MyBottariUiState,
    onClickPersonalBottari: (Long, String) -> Unit,
    onClickTeamBottari: (Long, String) -> Unit,
    onDeletePersonalBottari: (Long) -> Unit,
    onDeleteTeamBottari: (Long) -> Unit,
    onEditPersonalBottari: (Long) -> Unit,
    onEditTeamBottari: (Long) -> Unit,
    onOpenPersonalDialog: () -> Unit,
    onOpenTeamDialog: () -> Unit,
    onOpenCodeDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFabExpanded by remember { mutableStateOf(false) }
    var isFabVisible by remember { mutableStateOf(true) }
    var openedMenuBottariId by remember { mutableStateOf<Long?>(null) }

    val allListState = rememberLazyListState()
    val personalListState = rememberLazyListState()
    val teamListState = rememberLazyListState()

    LaunchedEffect(
        allListState.isScrollInProgress,
        personalListState.isScrollInProgress,
        teamListState.isScrollInProgress,
    ) {
        if (allListState.isScrollInProgress || personalListState.isScrollInProgress || teamListState.isScrollInProgress) {
            isFabVisible = false
            isFabExpanded = false
            return@LaunchedEffect
        }
        delay(500)
        isFabVisible = true
    }

    LaunchedEffect(uiState.isFetched) {
        allListState.scrollToItem(0)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val pageTitles =
                listOf(
                    stringResource(R.string.all_bottari_text),
                    stringResource(R.string.personal_bottari_text),
                    stringResource(R.string.team_bottari_text),
                )

            BottariTabBar(
                pageTitles = pageTitles,
                pagerState = rememberPagerState(initialPage = 0) { pageTitles.size },
            ) { page ->
                val onBottariClick = onBottariClick@{ bottari: MyBottariUiModel ->
                    if (isFabExpanded || openedMenuBottariId != null) {
                        isFabExpanded = false
                        openedMenuBottariId = null
                        return@onBottariClick
                    }
                    navigateToChecklist(
                        bottari,
                        onClickPersonalBottari,
                        onClickTeamBottari,
                    )
                }

                when (page) {
                    0 -> if (uiState.isAllEmpty) MyBottariEmptyView()
                    1 -> if (uiState.isPersonalEmpty) MyBottariEmptyView()
                    2 -> if (uiState.isTeamEmpty) MyBottariEmptyView()
                }

                val currentListState =
                    when (page) {
                        0 -> allListState
                        1 -> personalListState
                        2 -> teamListState
                        else -> error("유효하지 않은 페이지")
                    }

                val currentList =
                    when (page) {
                        0 -> uiState.allBottaries
                        1 -> uiState.personalBottaries
                        2 -> uiState.teamBottaries
                        else -> emptyList()
                    }

                BottariList(
                    bottaries = currentList,
                    listState = currentListState,
                    onBottariClick = onBottariClick,
                    onDeletePersonalBottari = onDeletePersonalBottari,
                    onDeleteTeamBottari = onDeleteTeamBottari,
                    onEditPersonalBottari = onEditPersonalBottari,
                    onEditTeamBottari = onEditTeamBottari,
                )
            }
        }

        if (uiState.isLoading) {
            IndeterminateCircularIndicator()
        }

        AnimatedVisibility(
            visible = isFabVisible && uiState.isLoading.not(),
            modifier = Modifier.align(Alignment.BottomEnd),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            BottariCreateFAB(
                onOpenPersonalDialog = onOpenPersonalDialog,
                onOpenTeamDialog = onOpenTeamDialog,
                onOpenCodeDialog = onOpenCodeDialog,
            )
        }
    }
}

private fun navigateToChecklist(
    bottari: MyBottariUiModel,
    onNavigateToPersonalChecklist: (Long, String) -> Unit,
    onNavigateToTeamChecklist: (Long, String) -> Unit,
) {
    when (bottari) {
        is BottariUiModel -> onNavigateToPersonalChecklist(bottari.id, bottari.title)
        is TeamBottariUiModel -> onNavigateToTeamChecklist(bottari.id, bottari.title)
    }
}

@Preview(showBackground = true)
@Composable
private fun MyBottariContentPreview() {
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
            onClickPersonalBottari = { _, _ -> },
            onClickTeamBottari = { _, _ -> },
            onDeletePersonalBottari = {},
            onDeleteTeamBottari = {},
            onEditPersonalBottari = { },
            onEditTeamBottari = { },
            onOpenPersonalDialog = {},
            onOpenTeamDialog = {},
            onOpenCodeDialog = {},
        )
    }
}
