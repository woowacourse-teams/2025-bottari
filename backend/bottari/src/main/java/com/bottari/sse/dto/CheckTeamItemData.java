package com.bottari.sse.dto;

import com.bottari.teambottari.event.CheckTeamAssignedItemEvent;
import com.bottari.teambottari.event.CheckTeamSharedItemEvent;
import java.time.LocalDateTime;

public record CheckTeamItemData(
        Long infoId,
        Long memberId,
        boolean isChecked,
        LocalDateTime publishedAt
) {

    public static CheckTeamItemData from(final CheckTeamSharedItemEvent event) {
        return new CheckTeamItemData(
                event.getInfoId(),
                event.getMemberId(),
                event.isChecked(),
                event.getPublishedAt()
        );
    }

    public static CheckTeamItemData from(final CheckTeamAssignedItemEvent event) {
        return new CheckTeamItemData(
                event.getInfoId(),
                event.getMemberId(),
                event.isChecked(),
                event.getPublishedAt()
        );
    }
}
