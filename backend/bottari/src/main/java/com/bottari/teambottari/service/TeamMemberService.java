package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.dto.ReadTeamMemberStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import com.bottari.teambottari.repository.TeamBottariRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamBottariRepository teamBottariRepository;
    private final TeamSharedItemRepository teamSharedItemRepository;
    private final TeamAssignedItemRepository teamAssignedItemRepository;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<ReadTeamMemberStatusResponse> getTeamMemberStatusByTeamBottariId(
            final Long teamBottariId,
            final String ssaid
    ) {
        validateTeamBottariExists(teamBottariId);
        final List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottariId);
        validateMemberInTeam(ssaid, teamMembers);

        return teamMembers.stream()
                .map(this::buildReadTeamMemberStatusResponse)
                .toList();
    }

    private void validateMemberInTeam(
            final String ssaid,
            final List<TeamMember> teamMembers
    ) {
        teamMembers.stream()
                .filter(teamMember -> teamMember.getMember().isSameBySsaid(ssaid))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI));
    }

    private void validateTeamBottariExists(final Long teamBottariId) {
        if (!teamBottariRepository.existsById(teamBottariId)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND);
        }
    }

    private ReadTeamMemberStatusResponse buildReadTeamMemberStatusResponse(final TeamMember teamMember) {
        final List<TeamSharedItem> sharedItems = teamSharedItemRepository.findAllByTeamMemberId(teamMember.getId());
        final List<TeamAssignedItem> assignedItems = teamAssignedItemRepository.findAllByTeamMemberId(
                teamMember.getId());
        final int totalItemsCount = sharedItems.size() + assignedItems.size();
        final List<TeamMemberItemResponse> sharedItemResponses = sharedItems.stream()
                .map(TeamMemberItemResponse::from)
                .toList();
        final List<TeamMemberItemResponse> assignedItemResponses = assignedItems.stream()
                .map(TeamMemberItemResponse::from)
                .toList();

        return new ReadTeamMemberStatusResponse(
                teamMember.getMember().getName(),
                teamMember.isOwner(),
                totalItemsCount,
                calculateCheckedItemsCount(sharedItems, assignedItems),
                sharedItemResponses,
                assignedItemResponses
        );
    }

    private int calculateCheckedItemsCount(
            final List<TeamSharedItem> sharedItems,
            final List<TeamAssignedItem> assignedItems
    ) {
        final int checkedSharedItemsCount = (int) sharedItems.stream()
                .filter(TeamSharedItem::isChecked)
                .count();
        final int checkedAssignedItemsCount = (int) assignedItems.stream()
                .filter(TeamAssignedItem::isChecked)
                .count();

        return checkedSharedItemsCount + checkedAssignedItemsCount;
    }
}
