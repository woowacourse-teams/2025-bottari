package com.bottari.dto;

import com.bottari.domain.Member;

public record CreateMemberRequest(
        String ssaid,
        String name
) {

    public Member toMember() {
        return new Member(ssaid, name);
    }
}
