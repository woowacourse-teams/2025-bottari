package com.bottari.sse.dto;

import com.bottari.teambottari.dto.ReadAssignedItemResponse;
import com.bottari.teambottari.event.CreateAssignedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record CreateAssignedItemInfoData(
        Long teamBottariId,
        List<ReadAssignedItemResponse> infos,
        LocalDateTime publishedAt
) {

    public static CreateAssignedItemInfoData of(
            final List<ReadAssignedItemResponse> infos,
            final CreateAssignedItemEvent event
    ) {
        return new CreateAssignedItemInfoData(
                event.getTeamBottariId(),
                infos,
                event.getPublishedAt()
        );
    }
}
