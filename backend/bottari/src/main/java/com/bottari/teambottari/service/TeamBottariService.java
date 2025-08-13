package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.InviteCodeGenerator;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamBottariPreviewResponse;
import com.bottari.teambottari.dto.TeamBottariMemberCountProjection;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import com.bottari.teambottari.repository.TeamBottariRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import com.bottari.teambottari.repository.TeamPersonalItemRepository;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamBottariService {

    private final TeamBottariRepository teamBottariRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;
    private final TeamSharedItemRepository teamSharedItemRepository;
    private final TeamAssignedItemRepository teamAssignedItemRepository;
    private final TeamPersonalItemRepository teamPersonalItemRepository;

    @Transactional(readOnly = true)
    public List<ReadTeamBottariPreviewResponse> getAllBySsaid(final String ssaid) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberId(member.getId());

        return buildReadTeamBottariPreviewResponses(teamMembers);
    }

    @Transactional
    public Long create(
            final String ssaid,
            final CreateTeamBottariRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        try {
            final String inviteCode = InviteCodeGenerator.generate();
            final TeamBottari teamBottari = new TeamBottari(request.title(), member, inviteCode);
            final TeamBottari savedTeamBottari = teamBottariRepository.save(teamBottari);
            final TeamMember teamMember = new TeamMember(savedTeamBottari, member);
            teamMemberRepository.save(teamMember);

            return savedTeamBottari.getId();
        } catch (final DataIntegrityViolationException exception) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_INVITE_CODE_GENERATION_FAILED);
        }
    }

    private List<ReadTeamBottariPreviewResponse> buildReadTeamBottariPreviewResponses(final List<TeamMember> teamMembers) {
        final Map<TeamMember, List<TeamSharedItem>> teamSharedItemsGroupByTeamMember = groupingTeamSharedItem(teamMembers);
        final Map<TeamMember, List<TeamAssignedItem>> teamAssignedItemsGroupByTeamMember = groupingTeamAssignedItem(teamMembers);
        final Map<TeamMember, List<TeamPersonalItem>> teamPersonalItemsGroupByTeamMember = groupingTeamPersonalItem(teamMembers);
        final Map<Long, Integer> memberCountsByTeamBottariId = getMembersCountByTeamBottariId(teamMembers);

        return teamMembers.stream()
                .map(teamMember -> {
                    final TeamBottari teamBottari = teamMember.getTeamBottari();
                    final List<TeamSharedItem> sharedItems = teamSharedItemsGroupByTeamMember.getOrDefault(teamMember, Collections.emptyList());
                    final List<TeamAssignedItem> assignedItems = teamAssignedItemsGroupByTeamMember.getOrDefault(teamMember, Collections.emptyList());
                    final List<TeamPersonalItem> personalItems = teamPersonalItemsGroupByTeamMember.getOrDefault(teamMember, Collections.emptyList());
                    final int totalItemsCount = sharedItems.size() + assignedItems.size() + personalItems.size();
                    final int checkedItemsCount = getCheckedItemsCount(sharedItems, assignedItems, personalItems);
                    final int memberCount = memberCountsByTeamBottariId.getOrDefault(teamBottari.getId(), 0);

                    return new ReadTeamBottariPreviewResponse(
                            teamBottari.getId(),
                            teamBottari.getTitle(),
                            totalItemsCount,
                            checkedItemsCount,
                            memberCount,
                            null // TODO: 알람 매핑 방향 의논 필요, 우선 null 반환
                    );
                })
                .toList();
    }

    private Map<Long, Integer> getMembersCountByTeamBottariId(final List<TeamMember> teamMembers) {
        final List<TeamBottari> teamBottaries = teamMembers.stream()
                .map(TeamMember::getTeamBottari)
                .distinct()
                .collect(Collectors.toList());
        final List<TeamBottariMemberCountProjection> teamMembersCount = teamMemberRepository.countMembersByTeamBottariIn(teamBottaries);

        return teamMembersCount.stream()
                .collect(Collectors.toMap(
                        TeamBottariMemberCountProjection::getTeamBottariId,
                        TeamBottariMemberCountProjection::getMemberCount
                ));
    }

    private int getCheckedItemsCount(
            final List<TeamSharedItem> sharedItems,
            final List<TeamAssignedItem> assignedItems,
            final List<TeamPersonalItem> personalItems
    ) {
        final long checkedSharedItemsCount = sharedItems.stream().filter(TeamSharedItem::isChecked).count();
        final long checkedAssignedItemsCount = assignedItems.stream().filter(TeamAssignedItem::isChecked).count();
        final long checkedPersonalItemsCount = personalItems.stream().filter(TeamPersonalItem::isChecked).count();

        return (int) (checkedSharedItemsCount + checkedAssignedItemsCount + checkedPersonalItemsCount);
    }

    private Map<TeamMember, List<TeamSharedItem>> groupingTeamSharedItem(final List<TeamMember> teamMembers) {
        final List<TeamSharedItem> allByTeamMemberIn = teamSharedItemRepository.findAllByTeamMemberIn(teamMembers);

        return allByTeamMemberIn.stream()
                .collect(Collectors.groupingBy(TeamSharedItem::getTeamMember));
    }

    private Map<TeamMember, List<TeamAssignedItem>> groupingTeamAssignedItem(final List<TeamMember> teamMembers) {
        final List<TeamAssignedItem> allByTeamMemberIn = teamAssignedItemRepository.findAllByTeamMemberIn(teamMembers);

        return allByTeamMemberIn.stream()
                .collect(Collectors.groupingBy(TeamAssignedItem::getTeamMember));
    }

    private Map<TeamMember, List<TeamPersonalItem>> groupingTeamPersonalItem(final List<TeamMember> teamMembers) {
        final List<TeamPersonalItem> allByTeamMemberIn = teamPersonalItemRepository.findAllByTeamMemberIn(teamMembers);

        return allByTeamMemberIn.stream()
                .collect(Collectors.groupingBy(TeamPersonalItem::getTeamMember));
    }
}
