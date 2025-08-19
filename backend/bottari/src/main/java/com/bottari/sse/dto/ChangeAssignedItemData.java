package com.bottari.sse.dto;

import com.bottari.teambottari.event.ChangeTeamAssignedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record ChangeAssignedItemData(
        Long assignedItemInfoId,
        String name,
        List<Long> assigneeIds,
        LocalDateTime publishedAt
) {

    public static ChangeAssignedItemData from(final ChangeTeamAssignedItemEvent event) {
        return new ChangeAssignedItemData(
                event.getAssignedItemInfoId(),
                event.getName(),
                event.getAssigneeIds(),
                event.getPublishedAt()
        );
    }
}
