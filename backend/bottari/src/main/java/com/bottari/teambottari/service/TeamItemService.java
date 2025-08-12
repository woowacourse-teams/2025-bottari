package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.dto.CheckTeamItemRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.repository.TeamMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamItemService {

    private final TeamSharedItemService teamSharedItemService;
    private final TeamAssignedItemService teamAssignedItemService;
    private final TeamPersonalItemService teamPersonalItemService;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    public TeamMemberChecklistResponse getCheckList(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamMember teamMember = getTeamMemberByTeamBottariIdAndSsaid(teamBottariId, ssaid);
        final List<TeamSharedItem> teamSharedItems = teamSharedItemService.getAllByTeamMember(teamMember);
        final List<TeamAssignedItem> teamAssignedItems = teamAssignedItemService.getAllByTeamMember(teamMember);
        final List<TeamPersonalItem> teamPersonalItems = teamPersonalItemService.getAllByTeamMember(teamMember);

        return TeamMemberChecklistResponse.of(teamSharedItems, teamAssignedItems, teamPersonalItems);
    }

    public void check(
            final Long id,
            final String ssaid,
            final CheckTeamItemRequest request
    ) {
        switch (request.type()) {
            case SHARED -> teamSharedItemService.check(id, ssaid);
            case ASSIGNED -> teamAssignedItemService.check(id, ssaid);
            case PERSONAL -> teamPersonalItemService.check(id, ssaid);
        }
    }

    public void uncheck(
            final Long id,
            final String ssaid,
            final CheckTeamItemRequest request
    ) {
        switch (request.type()) {
            case SHARED -> teamSharedItemService.uncheck(id, ssaid);
            case ASSIGNED -> teamAssignedItemService.uncheck(id, ssaid);
            case PERSONAL -> teamPersonalItemService.uncheck(id, ssaid);
        }
    }

    private TeamMember getTeamMemberByTeamBottariIdAndSsaid(
            final Long teamBottariId,
            final String ssaid
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));

        return teamMemberRepository.findByTeamBottariIdAndMemberId(teamBottariId, member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI));
    }
}
