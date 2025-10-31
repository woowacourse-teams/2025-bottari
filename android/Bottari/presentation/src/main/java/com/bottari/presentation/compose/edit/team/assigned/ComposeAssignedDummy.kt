package com.bottari.presentation.compose.edit.team.assigned

import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.personal.SelectableItemUiModel
import com.bottari.presentation.model.bottari.team.member.TeamMemberUiModel

val m1 = TeamMemberUiModel(id = 1L, nickname = "멤버1", isHost = true)
val m2 = TeamMemberUiModel(id = 2L, nickname = "멤버2", isHost = false)
val m3 = TeamMemberUiModel(id = 3L, nickname = "멤버3", isHost = false)
val m4 = TeamMemberUiModel(id = 4L, nickname = "멤버4", isHost = false)
val m5 = TeamMemberUiModel(id = 5L, nickname = "멤버5", isHost = false)
val m6 = TeamMemberUiModel(id = 6L, nickname = "멤버6", isHost = false)
val m7 = TeamMemberUiModel(id = 7L, nickname = "멤버7", isHost = false)
val m8 = TeamMemberUiModel(id = 8L, nickname = "멤버8", isHost = false)
val m9 = TeamMemberUiModel(id = 9L, nickname = "멤버9", isHost = false)
val m10 = TeamMemberUiModel(id = 10L, nickname = "멤버10", isHost = false)
val m11 = TeamMemberUiModel(id = 11L, nickname = "멤버11", isHost = false)
val m12 = TeamMemberUiModel(id = 12L, nickname = "멤버12", isHost = false)
val m13 = TeamMemberUiModel(id = 13L, nickname = "멤버13", isHost = false)
val m14 = TeamMemberUiModel(id = 14L, nickname = "멤버14", isHost = false)
val m15 = TeamMemberUiModel(id = 15L, nickname = "멤버15", isHost = false)
val m16 = TeamMemberUiModel(id = 16L, nickname = "멤버16", isHost = false)
val m17 = TeamMemberUiModel(id = 17L, nickname = "멤버17", isHost = false)
val m18 = TeamMemberUiModel(id = 18L, nickname = "멤버18", isHost = false)
val m19 = TeamMemberUiModel(id = 19L, nickname = "멤버19", isHost = false)
val m20 = TeamMemberUiModel(id = 20L, nickname = "멤버20", isHost = false)

val dummyMembers = listOf(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10)

val previewAssignedSelectableItemsLarge = listOf(
    SelectableItemUiModel(
        id = 2L, name = "고기 굽기",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m2, m3, m10)),
        isSelected = true
    ),
    SelectableItemUiModel(
        id = 3L, name = "설거지 담당 (저녁)",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m4, m6, m7, m8)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 4L, name = "장보기 (채소)",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m9, m11)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 5L, name = "텐트 설치",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m1, m2, m3, m4, m5)),
        isSelected = true
    ),
    SelectableItemUiModel(
        id = 6L, name = "총무 (회비 관리)",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m12)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 7L, name = "음악/블루투스 스피커",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m13, m14)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 8L, name = "사진/영상 촬영",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m15)),
        isSelected = true
    ),
    SelectableItemUiModel(
        id = 9L, name = "짐 나르기 (공용 짐)",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m1, m3, m5, m7, m9, m11)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 10L, name = "아침 식사 준비",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m16, m17, m18)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 11L, name = "분리수거 및 뒷정리",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m19, m20, m2, m4)),
        isSelected = true
    ),
    SelectableItemUiModel(
        id = 12L, name = "게임/레크레이션 준비",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m6, m12)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 13L, name = "간식 담당",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m8, m10, m13)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 14L, name = "주류/음료 담당",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m1, m14, m15)),
        isSelected = true
    ),
    SelectableItemUiModel(
        id = 15L, name = "불 피우기 (바베큐)",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m3, m7)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 16L, name = "요리 (메인 디시)",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m11, m16)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 17L, name = "요리 보조 (재료 손질)",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m17, m18, m19, m20)),
        isSelected = true
    ),
    SelectableItemUiModel(
        id = 18L, name = "아이들 돌보기",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m4, m8)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 19L, name = "구급약 담당",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m1)),
        isSelected = false
    ),
    SelectableItemUiModel(
        id = 20L, name = "숙소 예약 확인",
        type = BottariItemTypeUiModel.ASSIGNED(members = listOf(m1, m12)),
        isSelected = true
    )
)