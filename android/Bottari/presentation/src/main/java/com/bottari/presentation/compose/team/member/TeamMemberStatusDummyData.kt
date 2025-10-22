import com.bottari.presentation.compose.team.member.ComposeTeamMembersStatusUiState
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberStatusUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

val dummyMembers =
    listOf(
        TeamMemberStatusUiModel(
            member = TeamMemberUiModel(id = 1L, nickname = "멤버 1 (방장)", isHost = true),
            totalItemsCount = 15,
            checkedItemsCount = 10,
            sharedItems =
                listOf(
                    PersonalChecklistItemUiModel(id = 101L, name = "공유 아이템 1", isChecked = false),
                ),
            assignedItems =
                listOf(
                    PersonalChecklistItemUiModel(id = 201L, name = "담당 아이템 1", isChecked = true),
                ),
            isMe = false,
            isExpanded = false,
        ),
        TeamMemberStatusUiModel(
            member = TeamMemberUiModel(id = 2L, nickname = "멤버 2 (나)", isHost = false),
            totalItemsCount = 12,
            checkedItemsCount = 8,
            sharedItems =
                listOf(
                    PersonalChecklistItemUiModel(id = 102L, name = "공유 아이템 2", isChecked = true),
                ),
            assignedItems =
                listOf(
                    PersonalChecklistItemUiModel(id = 202L, name = "담당 아이템 2", isChecked = false),
                ),
            isMe = true,
            isExpanded = true,
        ),
        TeamMemberStatusUiModel(
            member = TeamMemberUiModel(id = 3L, nickname = "멤버 3", isHost = false),
            totalItemsCount = 7,
            checkedItemsCount = 7,
            sharedItems =
                listOf(
                    PersonalChecklistItemUiModel(id = 103L, name = "공유 아이템 3", isChecked = true),
                ),
            assignedItems =
                listOf(
                    PersonalChecklistItemUiModel(id = 203L, name = "담당 아이템 3", isChecked = true),
                ),
            isMe = false,
            isExpanded = false,
        ),
    )

val teamMemberStatusDummyUiState =
    ComposeTeamMembersStatusUiState(
        membersStatus = dummyMembers,
        myId = 2,
        selectedMember = dummyMembers[0],
    )
