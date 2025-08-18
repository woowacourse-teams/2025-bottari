package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateAssignedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long assignedItemId;
    private final String name;
    private final List<Long> memberIds;
}
