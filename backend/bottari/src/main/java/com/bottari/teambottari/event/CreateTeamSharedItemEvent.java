package com.bottari.teambottari.event;

public record CreateTeamSharedItemEvent(
        Long teamBottariId,
        Long infoId,
        String name
) {
}
