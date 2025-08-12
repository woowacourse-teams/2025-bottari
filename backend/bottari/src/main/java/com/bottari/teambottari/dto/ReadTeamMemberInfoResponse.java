package com.bottari.teambottari.dto;

import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import java.util.List;

public record ReadTeamMemberInfoResponse(
        String inviteCode,
        int teamMemberCount,
        String ownerName,
        List<String> teamMemberNames
) {

    public static ReadTeamMemberInfoResponse of(
            final TeamBottari teamBottari,
            final List<TeamMember> teamMembers
    ) {
        return new ReadTeamMemberInfoResponse(
                teamBottari.getInviteCode(),
                teamMembers.size(),
                teamBottari.getOwner().getName(),
                teamMembers.stream()
                        .map(teamMember -> teamMember.getMember().getName())
                        .toList()
        );
    }
}
