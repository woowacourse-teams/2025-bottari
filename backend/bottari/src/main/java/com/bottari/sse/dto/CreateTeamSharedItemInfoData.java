package com.bottari.sse.dto;

import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record CreateTeamSharedItemInfoData(
        List<ReadSharedItemResponse> infos,
        LocalDateTime publishedAt
) {

    public static CreateTeamSharedItemInfoData of(
            final List<ReadSharedItemResponse> infos,
            final CreateTeamSharedItemEvent event
    ) {
        return new CreateTeamSharedItemInfoData(
                infos,
                event.getPublishedAt()
        );
    }
}
