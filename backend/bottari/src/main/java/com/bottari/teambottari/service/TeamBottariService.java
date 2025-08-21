package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.FcmMessageConverter;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.InviteCodeGenerator;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamBottariPreviewResponse;
import com.bottari.teambottari.dto.ReadTeamBottariResponse;
import com.bottari.teambottari.event.ExitTeamMemberEvent;
import com.bottari.teambottari.repository.TeamAssignedItemInfoRepository;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import com.bottari.teambottari.repository.TeamBottariRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import com.bottari.teambottari.repository.TeamPersonalItemRepository;
import com.bottari.teambottari.repository.TeamSharedItemInfoRepository;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import com.bottari.teambottari.repository.dto.TeamBottariMemberCountProjection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final TeamSharedItemInfoRepository teamSharedItemInfoRepository;
    private final TeamAssignedItemInfoRepository teamAssignedItemInfoRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public List<ReadTeamBottariPreviewResponse> getAllBySsaid(final String ssaid) {
        final Member member = getMemberBySsaid(ssaid);
        final List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberId(member.getId());

        return buildReadTeamBottariPreviewResponses(teamMembers);
    }

    @Transactional(readOnly = true)
    public ReadTeamBottariResponse getById(
            final String ssaid,
            final Long id
    ) {
        final TeamBottari teamBottari = teamBottariRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND));
        final Member member = getMemberBySsaid(ssaid);
        final TeamMember teamMember = findTeamMemberByTeamBottariAndMember(teamBottari, member);
        final List<TeamSharedItemInfo> sharedItems = findSharedItemsByTeam(teamBottari.getId());
        final List<TeamAssignedItemInfo> assignedItems = findAssignedItemsByTeam(teamBottari.getId());
        final List<TeamPersonalItem> personalItems = findPersonalItemsByMember(teamMember.getId());

        return ReadTeamBottariResponse.of(
                teamBottari,
                sharedItems,
                assignedItems,
                personalItems,
                null // TODO: 알람 매핑 방향 의논 필요, 우선 null 반환
        );
    }

    @Transactional
    public Long create(
            final String ssaid,
            final CreateTeamBottariRequest request
    ) {
        final Member member = getMemberBySsaid(ssaid);
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

    @Transactional
    public void exit(
            final Long id,
            final String ssaid
    ) {
        final TeamBottari teamBottari = teamBottariRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND));
        final List<TeamMember> allTeamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottari.getId());
        final TeamMember exitTeamMember = findExitTeamMember(ssaid, allTeamMembers);
        deleteItemsByTeamMember(exitTeamMember);
        teamAssignedItemInfoRepository.deleteOrphanAssignedItemInfosByTeamBottariId(teamBottari.getId());
        final List<TeamMember> remainMembers = findRemainMembers(ssaid, allTeamMembers);
        transferOwnerIfNeeded(remainMembers, teamBottari, exitTeamMember);
        teamMemberRepository.deleteById(exitTeamMember.getId());
        deleteTeamBottariIfEmpty(remainMembers, teamBottari);
        applicationEventPublisher.publishEvent(new ExitTeamMemberEvent(teamBottari.getId(),exitTeamMember.getMember().getId()));
    }

    private List<TeamMember> findRemainMembers(
            final String ssaid,
            final List<TeamMember> allTeamMembers)
    {
        return allTeamMembers.stream()
                .filter(teamMember -> !teamMember.isSameBySsaid(ssaid))
                .collect(Collectors.toList());
    }

    private void deleteItemsByTeamMember(final TeamMember exitTeamMember) {
        teamPersonalItemRepository.deleteByTeamMemberId(exitTeamMember.getId());
        teamSharedItemRepository.deleteByTeamMemberId(exitTeamMember.getId());
        teamAssignedItemRepository.deleteByTeamMemberId(exitTeamMember.getId());
    }

    private void deleteTeamBottariIfEmpty(
            final List<TeamMember> remainMembers,
            final TeamBottari teamBottari
    ) {
        if (remainMembers.isEmpty()) {
            teamSharedItemInfoRepository.deleteByTeamBottariId(teamBottari.getId());
            teamBottariRepository.deleteById(teamBottari.getId());
        }
    }

    private void transferOwnerIfNeeded(
            final List<TeamMember> remainMembers,
            final TeamBottari teamBottari,
            final TeamMember exitTeamMember
    ) {
        if (!teamBottari.isOwner(exitTeamMember.getMember()) || remainMembers.isEmpty()) {
            return;
        }
        remainMembers.stream()
                .min(Comparator.comparing(TeamMember::getCreatedAt))
                .map(TeamMember::getMember)
                .ifPresent(teamBottari::changeOwner);
    }

    private Member getMemberBySsaid(final String ssaid) {
        return memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
    }

    private List<ReadTeamBottariPreviewResponse> buildReadTeamBottariPreviewResponses(
            final List<TeamMember> teamMembers) {
        final Map<TeamMember, List<TeamSharedItem>> teamSharedItemsGroup = groupingTeamSharedItem(teamMembers);
        final Map<TeamMember, List<TeamAssignedItem>> teamAssignedItemsGroup = groupingTeamAssignedItem(teamMembers);
        final Map<TeamMember, List<TeamPersonalItem>> teamPersonalItemsGroup = groupingTeamPersonalItem(teamMembers);
        final Map<Long, Integer> memberCountsByTeamBottariId = getMembersCountByTeamBottariId(teamMembers);

        return teamMembers.stream()
                .map(teamMember -> {
                    final TeamBottari teamBottari = teamMember.getTeamBottari();
                    final List<TeamSharedItem> sharedItems = teamSharedItemsGroup.getOrDefault(
                            teamMember,
                            Collections.emptyList()
                    );
                    final List<TeamAssignedItem> assignedItems = teamAssignedItemsGroup.getOrDefault(
                            teamMember,
                            Collections.emptyList()
                    );
                    final List<TeamPersonalItem> personalItems = teamPersonalItemsGroup.getOrDefault(
                            teamMember,
                            Collections.emptyList()
                    );
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
                .sorted((a, b) -> Long.compare(b.id(), a.id()))
                .toList();
    }

    private Map<Long, Integer> getMembersCountByTeamBottariId(final List<TeamMember> teamMembers) {
        final List<TeamBottari> teamBottaries = teamMembers.stream()
                .map(TeamMember::getTeamBottari)
                .distinct()
                .collect(Collectors.toList());
        final List<TeamBottariMemberCountProjection> teamMembersCount =
                teamMemberRepository.countMembersByTeamBottariIn(teamBottaries);

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

    private TeamMember findTeamMemberByTeamBottariAndMember(
            final TeamBottari teamBottari,
            final Member member
    ) {
        return teamMemberRepository.findByTeamBottariIdAndMemberId(teamBottari.getId(), member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI));
    }

    private List<TeamSharedItemInfo> findSharedItemsByTeam(final Long teamBottariId) {
        return teamSharedItemInfoRepository.findAllByTeamBottariId(teamBottariId);
    }

    private List<TeamAssignedItemInfo> findAssignedItemsByTeam(final Long teamBottariId) {
        return teamAssignedItemInfoRepository.findAllByTeamBottariId(teamBottariId);
    }

    private List<TeamPersonalItem> findPersonalItemsByMember(final Long teamMemberId) {
        return teamPersonalItemRepository.findAllByTeamMemberId(teamMemberId);
    }

    private TeamMember findExitTeamMember(
            final String ssaid,
            final List<TeamMember> teamMembers
    ) {
        return teamMembers.stream()
                .filter(teamMember -> teamMember.getMember().isSameBySsaid(ssaid))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI));
    }
}
