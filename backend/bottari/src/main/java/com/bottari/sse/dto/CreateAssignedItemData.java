package com.bottari.sse.dto;

import com.bottari.teambottari.event.CreateAssignedItemEvent;
import java.util.List;

public record CreateAssignedItemData(
        Long id,
        String name,
        List<Long> memberIds
) {

    public static CreateAssignedItemData from(final CreateAssignedItemEvent event) {
        return new CreateAssignedItemData(event.assignedItemId(), event.name(), event.memberIds());
    }
}
