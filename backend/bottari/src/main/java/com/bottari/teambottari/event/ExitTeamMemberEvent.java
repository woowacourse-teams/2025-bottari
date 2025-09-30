package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExitTeamMemberEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final String teamBottariName;
    private final Long exitMemberId;
    private final String exitMemberName;
    private final List<Long> remainMemberIds;
}
