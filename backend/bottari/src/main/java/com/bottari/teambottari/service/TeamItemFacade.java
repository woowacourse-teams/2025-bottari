package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.CheckTeamItemRequest;
import com.bottari.teambottari.dto.ReadTeamItemsResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.repository.TeamMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamItemFacade {

    private final TeamSharedItemService teamSharedItemService;
    private final TeamAssignedItemService teamAssignedItemService;
    private final TeamPersonalItemService teamPersonalItemService;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    // TODO: 본인이 팀 멤버인지 확인하는 로직 추가
    public ReadTeamItemsResponse getTeamItems(
            final Long teamBottariId,
            final String ssaid
    ) {
        final List<TeamItemStatusResponse> sharedItemResponses = teamSharedItemService.getAllWithMemberStatusByTeamBottariId(
                teamBottariId);
        final List<TeamItemStatusResponse> assignedItemResponses = teamAssignedItemService.getAllWithMemberStatusByTeamBottariId(
                teamBottariId);

        return ReadTeamItemsResponse.of(sharedItemResponses, assignedItemResponses);
    }

    public TeamMemberChecklistResponse getCheckList(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamMember teamMember = getTeamMemberByTeamBottariIdAndSsaid(teamBottariId, ssaid);
        final List<TeamMemberItemResponse> teamSharedItems = teamSharedItemService.getAllByTeamMember(teamMember);
        final List<TeamMemberItemResponse> teamAssignedItems = teamAssignedItemService.getAllByTeamMember(teamMember);
        final List<TeamMemberItemResponse> teamPersonalItems = teamPersonalItemService.getAllByTeamMember(teamMember);

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
