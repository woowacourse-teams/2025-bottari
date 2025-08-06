package com.bottari.fixture;

import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamBottari;

public enum TeamBottariFixture {


    TEAM_BOTTARI("title"),
    TEAM_BOTTARI_2("title2"),
    ANOTHER_TEAM_BOTTARI("another title"),
    ;

    private final String title;

    TeamBottariFixture(final String title) {
        this.title = title;
    }

    public TeamBottari get(final Member member) {
        return new TeamBottari(title, member, "inviteCode");
    }
}
