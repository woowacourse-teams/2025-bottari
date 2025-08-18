package com.bottari.teambottari.event;

public record CheckTeamSharedItemEvent(
        Long teamBottariId,
        Long memberId,
        Long infoId,
        Long itemId,
        boolean isChecked
) {
}
