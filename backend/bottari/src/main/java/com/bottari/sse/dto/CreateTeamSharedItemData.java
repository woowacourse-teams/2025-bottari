package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record CreateTeamSharedItemData(
        List<Long> itemIds,
        LocalDateTime publishedAt
) {

    public static CreateTeamSharedItemData from(final CreateTeamSharedItemEvent event) {
        return new CreateTeamSharedItemData(
                event.getItemIds(),
                event.getPublishedAt()
        );
    }
}
