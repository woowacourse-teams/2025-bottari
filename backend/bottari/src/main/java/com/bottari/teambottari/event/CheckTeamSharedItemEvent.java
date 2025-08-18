package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;

@Getter
public class CheckTeamSharedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long memberId;
    private final Long infoId;
    private final Long itemId;
    private final boolean isChecked;

    public CheckTeamSharedItemEvent(
            final Long teamBottariId,
            final Long memberId,
            final Long infoId,
            final Long itemId,
            final boolean isChecked
    ) {
        super();
        this.teamBottariId = teamBottariId;
        this.memberId = memberId;
        this.infoId = infoId;
        this.itemId = itemId;
        this.isChecked = isChecked;
    }
}
