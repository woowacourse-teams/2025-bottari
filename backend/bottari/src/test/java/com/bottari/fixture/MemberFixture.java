package com.bottari.fixture;

import com.bottari.domain.Member;

public enum MemberFixture {

    MEMBER("ssaid", "name"),
    ANOTHER_MEMBER("another_ssaid", "another_name")
    ;

    private final String ssaid;
    private final String name;

    MemberFixture(
            final String ssaid,
            final String name
    ) {
        this.ssaid = ssaid;
        this.name = name;
    }

    public Member getMember() {
        return new Member(ssaid, name);
    }
}
