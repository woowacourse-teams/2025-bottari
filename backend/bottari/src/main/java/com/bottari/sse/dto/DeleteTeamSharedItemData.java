package com.bottari.sse.dto;

import com.bottari.teambottari.event.DeleteTeamSharedItemEvent;
import java.time.LocalDateTime;

public record DeleteTeamSharedItemData(
        Long infoId,
        String name,
        LocalDateTime publishedAt
) {

    public static DeleteTeamSharedItemData from(final DeleteTeamSharedItemEvent event) {
        return new DeleteTeamSharedItemData(
                event.getInfoId(),
                event.getName(),
                event.getPublishedAt()
        );
    }
}
