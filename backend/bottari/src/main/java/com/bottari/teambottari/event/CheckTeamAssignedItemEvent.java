package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;

@Getter
public class CheckTeamAssignedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long memberId;
    private final Long infoId;
    private final Long itemId;
    private final boolean isChecked;

    public CheckTeamAssignedItemEvent(
            final Long teamBottariId,
            final Long memberId,
            final Long infoId,
            final Long itemId,
            final boolean isChecked
    ) {
        this.teamBottariId = teamBottariId;
        this.memberId = memberId;
        this.infoId = infoId;
        this.itemId = itemId;
        this.isChecked = isChecked;
    }
}
