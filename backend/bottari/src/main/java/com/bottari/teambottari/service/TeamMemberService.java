package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import com.bottari.teambottari.repository.TeamPersonalItemRepository;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final MemberRepository memberRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamSharedItemRepository teamSharedItemRepository;
    private final TeamAssignedItemRepository teamAssignedItemRepository;
    private final TeamPersonalItemRepository teamPersonalItemRepository;

    public TeamMemberChecklistResponse getCheckList(
            final Long teamBottariId,
            final String ssaid
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final TeamMember teamMember = teamMemberRepository.findByTeamBottariIdAndMemberId(teamBottariId, member.getId())
                .orElseThrow();
        final List<TeamSharedItem> teamSharedItems = teamSharedItemRepository.findAllByTeamMemberId(teamMember.getId());
        final List<TeamAssignedItem> teamAssignedItems = teamAssignedItemRepository.findAllByTeamMemberId(
                teamMember.getId());
        final List<TeamPersonalItem> teamPersonalItems = teamPersonalItemRepository.findAllByTeamMemberId(
                teamMember.getId());

        return TeamMemberChecklistResponse.of(teamSharedItems, teamAssignedItems, teamPersonalItems);
    }
}
