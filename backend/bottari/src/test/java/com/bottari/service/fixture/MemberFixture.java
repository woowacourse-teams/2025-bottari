package com.bottari.service.fixture;

import com.bottari.domain.Member;

public enum MemberFixture {

    MEMBER("ssaid", "name"),
    ANOTHER_MEMBER("another_ssaid", "another")
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

    public Member get() {
        return new Member(ssaid, name);
    }
}
