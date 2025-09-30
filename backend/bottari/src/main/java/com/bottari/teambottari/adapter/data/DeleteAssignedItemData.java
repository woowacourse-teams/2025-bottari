package com.bottari.teambottari.adapter.data;

import com.bottari.teambottari.event.DeleteAssignedItemEvent;
import java.time.LocalDateTime;

public record DeleteAssignedItemData(
        Long teamBottariId,
        LocalDateTime publishedAt
) {

    public static DeleteAssignedItemData from(final DeleteAssignedItemEvent event) {
        return new DeleteAssignedItemData(
                event.getTeamBottariId(),
                event.getPublishedAt()
        );
    }
}
