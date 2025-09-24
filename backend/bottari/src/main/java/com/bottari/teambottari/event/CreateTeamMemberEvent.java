package com.bottari.teambottari.event;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateTeamMemberEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long memberId;
    private final String name;
    private final boolean isOwner;
}
