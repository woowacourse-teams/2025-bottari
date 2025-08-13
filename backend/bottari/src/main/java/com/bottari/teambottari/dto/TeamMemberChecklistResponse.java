package com.bottari.teambottari.dto;

import java.util.List;

public record TeamMemberChecklistResponse(
        List<TeamMemberItemResponse> sharedItems,
        List<TeamMemberItemResponse> assignedItems,
        List<TeamMemberItemResponse> personalItems
) {

    public static TeamMemberChecklistResponse of(
            final List<TeamMemberItemResponse> sharedItems,
            final List<TeamMemberItemResponse> assignedItems,
            final List<TeamMemberItemResponse> personalItems
    ) {

        return new TeamMemberChecklistResponse(
                sharedItems,
                assignedItems,
                personalItems
        );
    }
}
