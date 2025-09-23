package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateAssignedItemEvent;
import java.time.LocalDateTime;

public record CreateAssignedItemData(
        Long teamBottariId,
        LocalDateTime publishedAt
) {

    public static CreateAssignedItemData from(final CreateAssignedItemEvent event) {
        return new CreateAssignedItemData(
                event.getTeamBottariId(),
                event.getPublishedAt()
        );
    }
}
