package com.bottari.sse.dto;

import com.bottari.teambottari.event.DeleteAssignedItemEvent;
import java.time.LocalDateTime;

public record DeleteAssignedItemData(
        Long id,
        String name,
        LocalDateTime publishedAt
) {

    public static DeleteAssignedItemData from(final DeleteAssignedItemEvent event) {
        return new DeleteAssignedItemData(event.getAssignedItemId(), event.getName(), event.getPublishedAt());
    }
}
