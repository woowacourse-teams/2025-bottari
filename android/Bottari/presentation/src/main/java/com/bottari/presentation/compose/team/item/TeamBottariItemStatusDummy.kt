package com.bottari.presentation.compose.team.item

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModelStatus
import com.bottari.presentation.model.bottari.team.member.MemberCheckStatusUiModel

val teamBottariItemStatusDummyUiState: ComposeTeamBottariItemStatusUiState =
    run {
        val dummyItems =
            listOf(
                TeamBottariUiModelStatus(
                    id = 1L,
                    name = "공용 물건 1 (수건)",
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "멤버 A", checked = true),
                            MemberCheckStatusUiModel(name = "멤버 B", checked = false),
                            MemberCheckStatusUiModel(name = "멤버 C", checked = true),
                        ),
                    checkItemsCount = 2,
                    totalItemsCount = 3,
                    type = BottariItemTypeUiModel.SHARED,
                ),
                TeamBottariUiModelStatus(
                    id = 2L,
                    name = "개인 할당 물건 (여권)",
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "멤버 B", checked = true),
                        ),
                    checkItemsCount = 1,
                    totalItemsCount = 1,
                    type = BottariItemTypeUiModel.ASSIGNED(),
                ),
                TeamBottariUiModelStatus(
                    id = 3L,
                    name = "공용 물건 2 (상비약)",
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "멤버 A", checked = false),
                            MemberCheckStatusUiModel(name = "멤버 B", checked = false),
                            MemberCheckStatusUiModel(name = "멤버 C", checked = false),
                        ),
                    checkItemsCount = 0,
                    totalItemsCount = 3,
                    type = BottariItemTypeUiModel.SHARED,
                ),
                TeamBottariUiModelStatus(
                    id = 4L,
                    name = "공용 물건 3 (충전기)",
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "멤버 A", checked = true),
                            MemberCheckStatusUiModel(name = "멤버 C", checked = true),
                        ),
                    checkItemsCount = 2,
                    totalItemsCount = 2,
                    type = BottariItemTypeUiModel.SHARED,
                ),
                TeamBottariUiModelStatus(
                    id = 5L,
                    name = "모두 챙긴 물건",
                    memberCheckStatus =
                        listOf(
                            MemberCheckStatusUiModel(name = "멤버 A", checked = true),
                            MemberCheckStatusUiModel(name = "멤버 B", checked = true),
                            MemberCheckStatusUiModel(name = "멤버 C", checked = true),
                        ),
                    checkItemsCount = 3,
                    totalItemsCount = 3,
                    type = BottariItemTypeUiModel.SHARED,
                ),
            )

        ComposeTeamBottariItemStatusUiState(
            isLoading = false,
            items = dummyItems,
            selectedProduct = dummyItems.firstOrNull(),
        )
    }
