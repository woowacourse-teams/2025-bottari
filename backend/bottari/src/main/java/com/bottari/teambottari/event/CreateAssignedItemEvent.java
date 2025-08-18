package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateAssignedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long assignedItemId;
    private final String name;
    private final List<Long> memberIds;

    public CreateAssignedItemEvent(
            final Long teamBottariId,
            final Long assignedItemId,
            final String name,
            final List<Long> memberIds
    ) {
        this.teamBottariId = teamBottariId;
        this.assignedItemId = assignedItemId;
        this.name = name;
        this.memberIds = memberIds;
    }
}
