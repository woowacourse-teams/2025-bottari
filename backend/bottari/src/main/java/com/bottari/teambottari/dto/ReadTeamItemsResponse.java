package com.bottari.teambottari.dto;

import java.util.List;

public record ReadTeamItemsResponse(
        List<TeamItemStatusResponse> sharedItems,
        List<TeamItemStatusResponse> assignedItems
) {

    public static ReadTeamItemsResponse of(
            final List<TeamItemStatusResponse> sharedItemResponses,
            final List<TeamItemStatusResponse> assignedItemResponses
    ) {
        return new ReadTeamItemsResponse(
                sharedItemResponses,
                assignedItemResponses
        );
    }
}
