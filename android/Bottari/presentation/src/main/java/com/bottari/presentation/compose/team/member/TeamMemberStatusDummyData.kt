package com.bottari.presentation.compose.team.member

import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberStatusUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

val teamMemberStatusDummyList =
    List(10) { index ->
        val memberId = index + 1
        val isHost = index == 0
        val isMe = index == 1
        val checkedCount = (5..15).random()
        val totalCount = checkedCount + (2..7).random()

        TeamMemberStatusUiModel(
            member =
                TeamMemberUiModel(
                    id = memberId.toLong(),
                    nickname = "멤버 $memberId",
                    isHost = isHost,
                ),
            totalItemsCount = totalCount,
            checkedItemsCount = checkedCount,
            sharedItems =
                listOf(
                    PersonalChecklistItemUiModel(
                        id = (100 + index).toLong(),
                        name = "공유 아이템 ${index + 1}",
                        isChecked = false,
                    ),
                ),
            assignedItems =
                listOf(
                    PersonalChecklistItemUiModel(
                        id = (200 + index).toLong(),
                        name = "담당 아이템 ${index + 1}",
                        isChecked = true,
                    ),
                ),
            isMe = isMe,
            isExpanded = false,
        )
    }

val teamMemberStatusDummyUiState =
    ComposeTeamMembersStatusUiState(
        membersStatus = teamMemberStatusDummyList,
        myId = 2,
        selectedMember = teamMemberStatusDummyList[0],
    )
