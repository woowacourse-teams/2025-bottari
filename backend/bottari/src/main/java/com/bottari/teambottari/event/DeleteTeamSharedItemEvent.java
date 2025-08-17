package com.bottari.teambottari.event;

public record DeleteTeamSharedItemEvent(
        Long teamBottariId,
        Long infoId,
        String name
) {
}
