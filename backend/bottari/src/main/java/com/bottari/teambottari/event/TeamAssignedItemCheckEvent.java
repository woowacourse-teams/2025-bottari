package com.bottari.teambottari.event;

public record TeamAssignedItemCheckEvent(
        Long teamBottariId,
        Long memberId,
        Long infoId,
        Long itemId,
        boolean isChecked
) {
}
