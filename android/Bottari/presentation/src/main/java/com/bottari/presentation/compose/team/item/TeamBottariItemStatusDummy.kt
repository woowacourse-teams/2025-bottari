package com.bottari.presentation.compose.team.item

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModelStatus
import com.bottari.presentation.model.bottari.team.member.MemberCheckStatusUiModel
import kotlin.random.Random

val teamBottariItemStatusDummyUiState =
    run {
        val dummySharedItems = createDummyProductList(10, BottariItemTypeUiModel.SHARED)
        val dummyAssignedItems = createDummyProductList(5, BottariItemTypeUiModel.ASSIGNED())

        ComposeTeamBottariItemStatusUiState(
            isLoading = false,
            items = dummySharedItems + dummyAssignedItems,
            selectedProduct = dummySharedItems.firstOrNull(),
        )
    }

private fun createDummyProductList(
    count: Int,
    type: BottariItemTypeUiModel,
    idStartIndex: Long = 0,
): List<TeamBottariUiModelStatus> =
    List(count) { index ->
        val memberCount = Random.nextInt(2, 6)
        val memberCheckStatus =
            List(memberCount) { memberIndex ->
                MemberCheckStatusUiModel(
                    name = "멤버 ${'A' + memberIndex}",
                    checked = Random.nextBoolean(),
                )
            }
        val checkedCount = memberCheckStatus.count { it.checked }

        TeamBottariUiModelStatus(
            id = idStartIndex + index,
            name = "더미 아이템 ${idStartIndex + index + 1}",
            memberCheckStatus = memberCheckStatus,
            checkItemsCount = checkedCount,
            totalItemsCount = memberCheckStatus.size,
            type = type,
        )
    }
