package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import java.util.List;

public record TeamMemberChecklistResponse(
        List<TeamMemberItemResponse> sharedItems,
        List<TeamMemberItemResponse> assignedItems,
        List<TeamMemberItemResponse> personalItems
) {

    public static TeamMemberChecklistResponse of(
            final List<TeamSharedItem> teamSharedItems,
            final List<TeamAssignedItem> teamAssignedItems,
            final List<TeamPersonalItem> teamPersonalItems
    ) {
        final List<TeamMemberItemResponse> sharedItemResponses = teamSharedItems.stream()
                .map(TeamMemberItemResponse::from)
                .toList();
        final List<TeamMemberItemResponse> assignedItemResponses = teamAssignedItems.stream()
                .map(TeamMemberItemResponse::from)
                .toList();
        final List<TeamMemberItemResponse> personalItemResponses = teamPersonalItems.stream()
                .map(TeamMemberItemResponse::from)
                .toList();

        return new TeamMemberChecklistResponse(
                sharedItemResponses,
                assignedItemResponses,
                personalItemResponses
        );
    }

    public record TeamMemberItemResponse(
            Long id,
            String name,
            boolean isChecked
    ) {

        public static TeamMemberItemResponse from(final TeamSharedItem teamSharedItem) {
            return new TeamMemberItemResponse(
                    teamSharedItem.getId(),
                    teamSharedItem.getInfo().getName(),
                    teamSharedItem.isChecked()
            );
        }

        public static TeamMemberItemResponse from(final TeamAssignedItem teamAssignedItem) {
            return new TeamMemberItemResponse(
                    teamAssignedItem.getId(),
                    teamAssignedItem.getInfo().getName(),
                    teamAssignedItem.isChecked()
            );
        }

        public static TeamMemberItemResponse from(final TeamPersonalItem teamPersonalItem) {
            return new TeamMemberItemResponse(
                    teamPersonalItem.getId(),
                    teamPersonalItem.getName(),
                    teamPersonalItem.isChecked()
            );
        }
    }
}
