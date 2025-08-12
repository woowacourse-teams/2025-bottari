package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.repository.TeamMemberRepository;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.repository.TeamBottariRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamBottariRepository teamBottariRepository;

    public ReadTeamMemberInfoResponse getTeamMemberInfoByTeamBottariId(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamBottari teamBottari = teamBottariRepository.findById(teamBottariId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND));
        final List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottariId);
        validateMemberInTeam(ssaid, teamMembers);

        return ReadTeamMemberInfoResponse.of(teamBottari, teamMembers);
    }

    private void validateMemberInTeam(
            final String ssaid,
            final List<TeamMember> teamMembers
    ) {
        teamMembers.stream()
                .map(teamMember -> teamMember.getMember().getSsaid())
                .filter(memberSsaid -> memberSsaid.equals(ssaid))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI));
    }
}
