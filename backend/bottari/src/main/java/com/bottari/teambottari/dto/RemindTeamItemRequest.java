package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamItemType;

public record RemindTeamItemRequest(
        TeamItemType type
) {
}
