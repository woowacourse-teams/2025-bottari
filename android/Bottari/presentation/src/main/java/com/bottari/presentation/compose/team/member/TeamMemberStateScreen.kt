package com.bottari.presentation.compose.team.member

import android.util.Log
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.bottari.presentation.model.bottari.team.member.TeamMemberStatusUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

@Composable
fun TeamMemberStateScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: ComposeTeamMembersStatusViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(null)

    LaunchedEffect(uiEvent) {
        when (uiEvent ?: return@LaunchedEffect) {
            ComposeTeamMembersStatusUiEvent.FetchMembersStatusFailure ->
                snackbarHostState.showSnackbar("보따리 불러오기에 실패했습니다")
            is ComposeTeamMembersStatusUiEvent.SendRemindByMemberMessageSuccess ->
                snackbarHostState.showSnackbar("보채기에 성공했어요")
            ComposeTeamMembersStatusUiEvent.SendRemindByMemberMessageFailure ->
                {
                    Log.d("test", "failed")
                    snackbarHostState.showSnackbar("보채기에 실패했어요")
                }
            ComposeTeamMembersStatusUiEvent.FetchMemberIdFailure ->
                snackbarHostState.showSnackbar("내 id를 불러오지 못했어요")
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
                        TeamMemberStateCard(
                            memberStatus = member,
                            onClick = { onSelectMember(member) },
                        )
                    }
                }
            }
        }
        uiState.selectedMember?.let { member ->
            TeamSendRemindDialog(
                title = member.member.nickname,
                isRemindable = (!member.isMe && !member.isAllChecked),
                onDismissRequest = { onSelectMember(null) },
                onClickRemind = { onSendRemind(member.member) },
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall)) {
                    if (member.checkedItems.isNotEmpty()) {
                        TeamStateListBox(
                            text = "해당 물건을 챙겼습니다",
                            painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
                            color = BottariTheme.colors.primary,
                            items = member.checkedItems.map { item -> item.name },
                        )
                    }
                    if (member.unCheckedItems.isNotEmpty()) {
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
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamMemberStateScreenPreview() {
    TeamMemberStateScreen(uiState = teamMemberStatusDummyUiState, onSelectMember = {}, onSendRemind = {})
}
