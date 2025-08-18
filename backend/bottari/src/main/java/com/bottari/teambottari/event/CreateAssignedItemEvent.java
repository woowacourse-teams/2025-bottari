package com.bottari.teambottari.event;

import java.util.List;

public record CreateAssignedItemEvent(
        Long teamBottariId,
        Long assignedItemId,
        String name,
        List<Long> memberIds
) {
}
