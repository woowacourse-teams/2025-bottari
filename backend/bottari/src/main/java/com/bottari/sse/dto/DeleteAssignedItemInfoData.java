package com.bottari.sse.dto;

import com.bottari.teambottari.dto.ReadAssignedItemResponse;
import com.bottari.teambottari.event.DeleteAssignedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record DeleteAssignedItemInfoData(
        Long teamBottariId,
        List<ReadAssignedItemResponse> infos,
        LocalDateTime publishedAt
) {

    public static DeleteAssignedItemInfoData of(
            final List<ReadAssignedItemResponse> infos,
            final DeleteAssignedItemEvent event
    ) {
        return new DeleteAssignedItemInfoData(
                event.getTeamBottariId(),
                infos,
                event.getPublishedAt()
        );
    }
}
