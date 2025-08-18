package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;

@Getter
public class DeleteTeamSharedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long infoId;
    private final String name;

    public DeleteTeamSharedItemEvent(
            final Long teamBottariId,
            final Long infoId,
            final String name
    ) {
        super();
        this.teamBottariId = teamBottariId;
        this.infoId = infoId;
        this.name = name;
    }
}
