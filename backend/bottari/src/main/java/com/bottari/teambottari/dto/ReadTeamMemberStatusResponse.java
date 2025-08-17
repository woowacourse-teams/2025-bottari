package com.bottari.teambottari.dto;

import java.util.List;

public record ReadTeamMemberStatusResponse(
        Long memberId,
        String teamMemberName,
        boolean isOwner,
        int totalItemsCount,
        int checkedItemsCount,
        List<TeamMemberItemResponse> sharedItems,
        List<TeamMemberItemResponse> assignedItems
) {
}
