package com.bottari.sse.dto;

import com.bottari.teambottari.event.DeleteTeamSharedItemEvent;

public record DeleteTeamSharedItemData(
        Long infoId,
        String name
) {

    public static DeleteTeamSharedItemData from(final DeleteTeamSharedItemEvent event) {
        return new DeleteTeamSharedItemData(
                event.infoId(),
                event.name()
        );
    }
}
