package com.bottari.feature.team.checklist.member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bottari.bottari.designsystem.component.BottariCircularLoader
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.model.bottari.team.TeamChecklistItemUiModel
import com.bottari.core.ui.model.bottari.team.member.TeamMemberStatusUiModel
import com.bottari.core.ui.model.bottari.team.member.TeamMemberUiModel
import com.bottari.feature.team.checklist.TeamSendRemindDialog
import com.bottari.feature.team.checklist.TeamStateCard
import com.bottari.feature.team.checklist.TeamStateListBox
import com.bottari.feature.team.checklist.checklist.TeamChecklistEmptyView
import teamMemberStatusDummyUiState
import com.bottari.core.ui.R as UIR

@Composable
fun TeamMemberStateScreen(
    bottariId: Long,
    checkedState: List<TeamChecklistItemUiModel>,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamMembersStatusViewModel =
        hiltViewModel<ComposeTeamMembersStatusViewModel, ComposeTeamMembersStatusViewModel.Factory> {
            it.create(bottariId)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(checkedState) { viewModel.fetchMemberId() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                ComposeTeamMembersStatusUiEvent.FetchMembersStatusFailure -> {
                    snackbarHostState.showSnackbar("보따리를 불러오지 못했어요")
                }

                is ComposeTeamMembersStatusUiEvent.SendRemindByMemberMessageSuccess -> {
                    snackbarHostState.showSnackbar("보채기에 성공했어요")
                }

                ComposeTeamMembersStatusUiEvent.SendRemindByMemberMessageFailure -> {
                    snackbarHostState.showSnackbar("보채기에 실패했어요")
                }
            }
        }
    }

    TeamMemberStateScreen(
        uiState = uiState,
        modifier = modifier,
        onSelectMember = viewModel::selectMember,
        onSendRemind = { member -> viewModel.debouncedSendRemindMessage(member) },
    )
}

@Composable
private fun TeamMemberStateScreen(
    uiState: ComposeTeamMembersStatusUiState,
    onSelectMember: (TeamMemberStatusUiModel?) -> Unit,
    onSendRemind: (TeamMemberUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxSize()
            .padding(BottariTheme.spacing.spaceMedium),
    ) {
        if (uiState.isInitialLoading) {
            BottariCircularLoader()
            return@Box
        }

        Column(verticalArrangement = Arrangement.Top) {
            Row {
                TeamStateCard(
                    title = "완료 인원",
                    value = uiState.checkedMembers.size.toString(),
                    painter = painterResource(UIR.drawable.ic_complete),
                    color = BottariTheme.colors.green,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
                TeamStateCard(
                    title = "미완료 인원",
                    value = uiState.uncheckedMembers.size.toString(),
                    painter = painterResource(UIR.drawable.ic_close),
                    color = BottariTheme.colors.red,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xLarge))

            LazyColumn(
                contentPadding = PaddingValues(bottom = BottariTheme.spacing.spaceMedium),
                verticalArrangement =
                    Arrangement.spacedBy(
                        BottariTheme.spacing.spaceXSmall,
                    ),
            ) {
                items(
                    items = uiState.membersStatus,
                    key = { "${it.member.id} ${it.member.nickname}" },
                ) { member ->
                    TeamMemberStateCard(
                        memberStatus = member,
                        onClick = { onSelectMember(member) },
                    )
                }
            }
        }
        uiState.selectedMember?.let { member ->
            TeamSendRemindDialog(
                title = member.member.nickname,
                enableRemind = member.shouldHurryUp,
                onDismissRequest = { onSelectMember(null) },
                onClickRemind = { onSendRemind(member.member) },
            ) {
                if (member.isItemsEmpty) {
                    TeamChecklistEmptyView(modifier = Modifier.fillMaxWidth())
                    return@TeamSendRemindDialog
                }
                Column(verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall)) {
                    if (member.checkedItems.isNotEmpty()) {
                        TeamStateListBox(
                            text = "해당 물건을 챙겼습니다",
                            painter = painterResource(id = UIR.drawable.ic_bottari_item_empty_view),
                            color = BottariTheme.colors.primary,
                            items = member.checkedItems.map { item -> item.name },
                        )
                    }
                    if (member.unCheckedItems.isNotEmpty()) {
                        TeamStateListBox(
                            text = "해당 물건을 챙기지 않았습니다.",
                            painter = painterResource(id = UIR.drawable.ic_bottari_item_empty_view),
                            color = BottariTheme.colors.red,
                            items = member.unCheckedItems.map { item -> item.name },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamMemberStateScreenPreview() {
    TeamMemberStateScreen(
        uiState = teamMemberStatusDummyUiState,
        onSelectMember = {},
        onSendRemind = {},
    )
}
