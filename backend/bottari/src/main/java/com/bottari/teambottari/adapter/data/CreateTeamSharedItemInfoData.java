package com.bottari.teambottari.adapter.data;

import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.event.CreateTeamSharedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record CreateTeamSharedItemInfoData(
        Long teamBottariId,
        List<ReadSharedItemResponse> infos,
        LocalDateTime publishedAt
) {

    public static CreateTeamSharedItemInfoData of(
            final List<ReadSharedItemResponse> infos,
            final CreateTeamSharedItemEvent event
    ) {
        return new CreateTeamSharedItemInfoData(
                event.getTeamBottariId(),
                infos,
                event.getPublishedAt()
        );
    }
}
