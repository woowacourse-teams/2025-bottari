package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import java.time.LocalDateTime;

public record CreateTeamSharedItemInfoData(
        Long infoId,
        String name,
        LocalDateTime publishedAt
) {

    public static CreateTeamSharedItemInfoData from(final CreateTeamSharedItemEvent event) {
        return new CreateTeamSharedItemInfoData(
                event.getInfoId(),
                event.getName(),
                event.getPublishedAt()
        );
    }
}
