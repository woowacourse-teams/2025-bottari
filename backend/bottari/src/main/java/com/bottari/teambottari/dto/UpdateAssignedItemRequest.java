package com.bottari.teambottari.dto;

import java.util.List;

public record UpdateAssignedItemRequest(
        String name,
        List<Long> assigneeIds
) {
}
