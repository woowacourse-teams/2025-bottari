package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import java.time.LocalDateTime;

public record CreateTeamSharedItemData(
        Long infoId,
        String name,
        LocalDateTime publishedAt
) {

    public static CreateTeamSharedItemData from(final CreateTeamSharedItemEvent event) {
        return new CreateTeamSharedItemData(
                event.getInfoId(),
                event.getName(),
                event.getPublishedAt()
        );
    }
}
