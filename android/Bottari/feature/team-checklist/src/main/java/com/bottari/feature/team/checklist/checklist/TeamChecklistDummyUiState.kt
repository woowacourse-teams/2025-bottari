package com.bottari.feature.team.checklist.checklist

import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.core.ui.model.bottari.team.TeamChecklistItemUiModel

val teamChecklistDummyUiState =
    ComposeTeamChecklistUiState(
        isLoading = false,
        bottariItems =
            listOf(
                TeamChecklistItemUiModel(
                    id = 0,
                    name = "더미 아이템 1 (공용)",
                    isChecked = false,
                    type = BottariItemTypeUiModel.SHARED,
                ),
                TeamChecklistItemUiModel(
                    id = 1,
                    name = "더미 아이템 2 (공용)",
                    isChecked = true,
                    type = BottariItemTypeUiModel.SHARED,
                ),
                TeamChecklistItemUiModel(
                    id = 2,
                    name = "더미 아이템 3 (공용)",
                    isChecked = false,
                    type = BottariItemTypeUiModel.SHARED,
                ),
                TeamChecklistItemUiModel(
                    id = 10,
                    name = "더미 아이템 11 (개인)",
                    isChecked = true,
                    type = BottariItemTypeUiModel.PERSONAL,
                ),
                TeamChecklistItemUiModel(
                    id = 11,
                    name = "더미 아이템 12 (개인)",
                    isChecked = false,
                    type = BottariItemTypeUiModel.PERSONAL,
                ),
                TeamChecklistItemUiModel(
                    id = 20,
                    name = "더미 아이템 21 (지정)",
                    isChecked = false,
                    type = BottariItemTypeUiModel.ASSIGNED(),
                ),
                TeamChecklistItemUiModel(
                    id = 21,
                    name = "더미 아이템 22 (지정)",
                    isChecked = true,
                    type = BottariItemTypeUiModel.ASSIGNED(),
                ),
            ),
        sections =
            mapOf(
                BottariItemTypeUiModel.SHARED to true,
                BottariItemTypeUiModel.PERSONAL to true,
                BottariItemTypeUiModel.ASSIGNED() to true,
            ),
    )
