package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import java.time.LocalDateTime;

public record CreateTeamSharedItemData(
        Long teamBottariId,
        LocalDateTime publishedAt
) {

    public static CreateTeamSharedItemData from(final CreateTeamSharedItemEvent event) {
        return new CreateTeamSharedItemData(
                event.getTeamBottariId(),
                event.getPublishedAt()
        );
    }
}
