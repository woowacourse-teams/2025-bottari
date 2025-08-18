package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;

@Getter
public class DeleteAssignedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long assignedItemId;
    private final String name;

    public DeleteAssignedItemEvent(
            final Long teamBottariId,
            final Long assignedItemId,
            final String name
    ) {
        super();
        this.teamBottariId = teamBottariId;
        this.assignedItemId = assignedItemId;
        this.name = name;
    }
}
