package com.bottari.teambottari.service;

import com.bottari.support.CustomApplicationEvent;
import lombok.Getter;

@Getter
public class CreateTeamMemberEvent extends CustomApplicationEvent {

    private final Long teamBottariId;
    private final Long memberId;
    private final String name;
    private final boolean isOwner;

    public CreateTeamMemberEvent(
            final Long teamBottariId,
            final Long memberId,
            final String name,
            final boolean isOwner
    ) {
        super();
        this.teamBottariId = teamBottariId;
        this.memberId = memberId;
        this.name = name;
        this.isOwner = isOwner;
    }
}
