package com.bottari.sse.dto;

import com.bottari.teambottari.event.CheckTeamAssignedItemEvent;
import com.bottari.teambottari.event.CheckTeamSharedItemEvent;

public record CheckTeamItemData(
        Long infoId,
        Long memberId,
        boolean isChecked
) {

    public static CheckTeamItemData from(
            final CheckTeamSharedItemEvent event
    ) {
        return new CheckTeamItemData(
                event.infoId(),
                event.memberId(),
                event.isChecked()
        );
    }

    public static CheckTeamItemData from(
            final CheckTeamAssignedItemEvent event
    ) {
        return new CheckTeamItemData(
                event.infoId(),
                event.memberId(),
                event.isChecked()
        );
    }
}
