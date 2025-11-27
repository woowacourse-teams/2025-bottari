package com.bottari.teambottari.adapter.data;

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
