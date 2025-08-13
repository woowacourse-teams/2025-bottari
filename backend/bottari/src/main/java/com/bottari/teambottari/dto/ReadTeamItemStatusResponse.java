package com.bottari.teambottari.dto;

import java.util.List;

public record ReadTeamItemStatusResponse(
        List<TeamItemStatusResponse> sharedItems,
        List<TeamItemStatusResponse> assignedItems
) {

    public static ReadTeamItemStatusResponse of(
            final List<TeamItemStatusResponse> sharedItemResponses,
            final List<TeamItemStatusResponse> assignedItemResponses
    ) {
        return new ReadTeamItemStatusResponse(
                sharedItemResponses,
                assignedItemResponses
        );
    }
}
