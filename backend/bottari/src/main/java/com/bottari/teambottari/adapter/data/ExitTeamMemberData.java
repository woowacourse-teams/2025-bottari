package com.bottari.teambottari.adapter.data;

import com.bottari.teambottari.event.ExitTeamMemberEvent;
import java.time.LocalDateTime;

public record ExitTeamMemberData(
        Long bottariId,
        String bottariName,
        Long exitMemberId,
        String exitMemberName,
        LocalDateTime publishedAt
) {

    public static ExitTeamMemberData from(final ExitTeamMemberEvent event) {
        return new ExitTeamMemberData(
                event.getTeamBottariId(),
                event.getTeamBottariName(),
                event.getExitMemberId(),
                event.getExitMemberName(),
                event.getPublishedAt()
        );
    }
}
