package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChangeTeamAssignedItemEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long infoId;
    private final String name;
    private final List<Long> memberIds;
}
