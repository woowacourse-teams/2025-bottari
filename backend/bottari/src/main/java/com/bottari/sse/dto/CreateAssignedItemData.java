package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateAssignedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record CreateAssignedItemData(
        Long id,
        String name,
        List<Long> memberIds,
        LocalDateTime publishedAt
) {

    public static CreateAssignedItemData from(final CreateAssignedItemEvent event) {
        return new CreateAssignedItemData(
                event.getAssignedItemId(),
                event.getName(),
                event.getMemberIds(),
                event.getPublishedAt()
        );
    }
}
