package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateTeamSharedItemEvent;

public record CreateTeamSharedItemData(
        Long infoId,
        String name
) {

    public static CreateTeamSharedItemData from(final CreateTeamSharedItemEvent event) {
        return new CreateTeamSharedItemData(
                event.infoId(),
                event.name()
        );
    }
}
