package com.bottari.presentation.compose.team.checklist

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel
import kotlin.random.Random

val teamChecklistDummyUiState =
    ComposeTeamChecklistUiState(
        isLoading = false,
        bottariItems =
            createDummyProductList(
                count = 10,
                type = BottariItemTypeUiModel.SHARED,
                idStartIndex = 0,
            ) +
                createDummyProductList(
                    count = 10,
                    type = BottariItemTypeUiModel.PERSONAL,
                    idStartIndex = 10,
                ) +
                createDummyProductList(
                    count = 10,
                    type = BottariItemTypeUiModel.ASSIGNED(),
                    idStartIndex = 20,
                ),
        sections =
            mapOf(
                BottariItemTypeUiModel.SHARED to true,
                BottariItemTypeUiModel.ASSIGNED() to true,
                BottariItemTypeUiModel.PERSONAL to true,
            ),
    )

private fun createDummyProductList(
    count: Int,
    type: BottariItemTypeUiModel,
    idStartIndex: Long = 0,
): List<TeamChecklistItemUiModel> =
    List(count) { index ->
        TeamChecklistItemUiModel(
            id = idStartIndex + index,
            name = "더미 아이템 ${idStartIndex + index + 1}",
            isChecked = Random.nextBoolean(),
            type = type,
        )
    }
