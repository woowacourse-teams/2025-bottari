package com.bottari.teambottari.dto;

import java.util.List;

public record CreateTeamAssignedItemRequest(
        String name,
        List<String> teamMemberNames
) {
}
