package com.bottari.sse.dto;

import com.bottari.teambottari.service.CreateTeamMemberEvent;
import java.time.LocalDateTime;

public record CreateTeamMemberData(
        LocalDateTime publishedAt,
        Long memberId,
        String name,
        boolean isOwner
) {

    public static CreateTeamMemberData from(final CreateTeamMemberEvent event) {
        return new CreateTeamMemberData(
                event.getPublishedAt(),
                event.getMemberId(),
                event.getName(),
                event.isOwner()
        );
    }
}
