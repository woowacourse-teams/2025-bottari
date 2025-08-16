package com.bottari.teambottari.dto;

import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamMember;

public record ReadTeamMemberNameResponse(
        Long id,
        String name
) {

    public static ReadTeamMemberNameResponse of(final TeamMember teamMember) {
        final Member member = teamMember.getMember();

        return new ReadTeamMemberNameResponse(member.getId(), member.getName());
    }
}
