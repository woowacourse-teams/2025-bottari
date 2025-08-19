package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DeleteAssignedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long assignedItemId;
    private final String name;
}
