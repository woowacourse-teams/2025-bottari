package com.bottari.sse.dto;

import com.bottari.teambottari.event.ChangeTeamAssignedItemEvent;
import java.time.LocalDateTime;
import java.util.List;

public record ChangeAssignedItemData(
        Long infoId,
        String name,
        List<Long> memberIds,
        LocalDateTime publishedAt
) {

    public static ChangeAssignedItemData from(final ChangeTeamAssignedItemEvent event) {
        return new ChangeAssignedItemData(
                event.getInfoId(),
                event.getName(),
                event.getMemberIds(),
                event.getPublishedAt()
        );
    }
}
