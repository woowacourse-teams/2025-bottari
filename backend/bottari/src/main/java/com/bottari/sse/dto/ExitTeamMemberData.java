package com.bottari.sse.dto;

import com.bottari.teambottari.event.ExitTeamMemberEvent;
import java.time.LocalDateTime;

public record ExitTeamMemberData(
        Long bottariId,
        Long exitMemberId,
        LocalDateTime publishedAt
) {

    public static ExitTeamMemberData from(final ExitTeamMemberEvent event) {
        return new ExitTeamMemberData(event.getTeamBottariId(), event.getExitMemberId(), event.getPublishedAt());
    }
}
