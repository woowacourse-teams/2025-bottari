package com.bottari.teambottari.event;

public record CheckTeamAssignedItemEvent(
        Long teamBottariId,
        Long memberId,
        Long infoId,
        Long itemId,
        boolean isChecked
) {
}
