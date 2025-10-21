package com.bottari.presentation.compose.team.member

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.team.TeamSendRemindDialog
import com.bottari.presentation.compose.team.TeamStateCard
import com.bottari.presentation.compose.team.TeamStateListBox

@Composable
fun TeamMemberStateScreen(
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamMembersStatusViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)

    TeamMemberStateScreen(uiState = uiState.value, modifier = modifier)
}

@Composable
fun TeamMemberStateScreen(
    uiState: ComposeTeamMembersStatusUiState,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize().padding(BottariTheme.spacing.spaceMedium)) {
        Column(verticalArrangement = Arrangement.Top) {
            Row {
                TeamStateCard(
                    title = "완료 인원",
                    value = uiState.checkedMembers.size.toString(),
                    painter = painterResource(R.drawable.ic_compleate),
                    color = BottariTheme.colors.green,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
                TeamStateCard(
                    title = "미완료 인원",
                    value = uiState.uncheckedMembers.size.toString(),
                    painter = painterResource(R.drawable.ic_close),
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
                uiState.membersStatus.forEach { member ->
                    item {
                        TeamMemberStateCard(memberStatus = member)
                    }
                }
            }
        }
        uiState.selectedMember?.let { member ->
            TeamSendRemindDialog(title = member.member.nickname, onDismissRequest = {}) {
                TeamStateListBox(
                    text = "해당 물건을 챙겼습니다",
                    painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
                    color = BottariTheme.colors.primary,
                    items = member.checkedItems.map { item -> item.name },
                )
                Spacer(Modifier.height(BottariTheme.spacing.spaceXSmall))
                TeamStateListBox(
                    text = "해당 물건을 챙기지 않았습니다.",
                    painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
                    color = BottariTheme.colors.red,
                    items = member.unCheckedItems.map { item -> item.name },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamMemberStateScreenPreview() {
    TeamMemberStateScreen(uiState = dummy)
}

private val dummy =
    ComposeTeamMembersStatusUiState(
        membersStatus = dummyData,
        myId = 2,
        selectedMember = dummyData[0],
    )
