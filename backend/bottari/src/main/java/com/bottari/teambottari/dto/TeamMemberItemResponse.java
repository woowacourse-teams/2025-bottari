package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;

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
