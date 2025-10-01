package com.bottari.teambottari.adapter.data;

import com.bottari.teambottari.event.DeleteTeamSharedItemEvent;
import java.time.LocalDateTime;

public record DeleteTeamSharedItemData(
        Long teamBottariId,
        LocalDateTime publishedAt
) {

    public static DeleteTeamSharedItemData from(final DeleteTeamSharedItemEvent event) {
        return new DeleteTeamSharedItemData(
                event.getTeamBottariId(),
                event.getPublishedAt()
        );
    }
}
