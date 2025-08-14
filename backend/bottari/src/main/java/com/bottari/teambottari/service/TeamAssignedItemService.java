package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.CreateTeamAssignedItemRequest;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.repository.TeamAssignedItemInfoRepository;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamAssignedItemService {

    private final TeamAssignedItemRepository teamAssignedItemRepository;
    private final TeamAssignedItemInfoRepository teamAssignedItemInfoRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(
            final TeamMember teamMember,
            final CreateTeamAssignedItemRequest request
    ) {
        final TeamBottari teamBottari = teamMember.getTeamBottari();
        validateDuplicateName(teamBottari.getId(), request.name());
        final TeamAssignedItemInfo savedTeamAssignedItemInfo = saveTeamAssignedItemInfo(request.name(), teamBottari);
        final List<TeamMember> teamMembers = findAssignedTeamMembersByRequest(request.teamMemberNames(), teamBottari);
        saveAssignedItemToTeamMembers(savedTeamAssignedItemInfo, teamMembers);

        return savedTeamAssignedItemInfo.getId();
    }

    @Transactional
    public void delete(
            final Long id,
            final String ssaid
    ) {
        final TeamAssignedItemInfo teamAssignedItemInfo = teamAssignedItemInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "담당"));
        validateMemberInTeam(teamAssignedItemInfo.getTeamBottari().getId(), ssaid);
        teamAssignedItemRepository.deleteAllByInfo(teamAssignedItemInfo);
        teamAssignedItemInfoRepository.delete(teamAssignedItemInfo);
    }

    public List<TeamItemStatusResponse> getAllWithMemberStatusByTeamBottariId(final Long teamBottariId) {
        final List<TeamAssignedItem> items = teamAssignedItemRepository.findAllByTeamBottariId(teamBottariId);
        final Map<TeamAssignedItemInfo, List<TeamAssignedItem>> itemsByInfo = groupByInfo(items);

        return itemsByInfo.keySet()
                .stream()
                .sorted(Comparator.comparing(TeamAssignedItemInfo::getCreatedAt))
                .map(info -> buildTeamStatusResponse(itemsByInfo, info))
                .toList();
    }

    public List<TeamMemberItemResponse> getAllByTeamMember(final TeamMember teamMember) {
        final List<TeamAssignedItem> items = teamAssignedItemRepository.findAllByTeamMemberId(teamMember.getId());

        return items.stream()
                .map(TeamMemberItemResponse::from)
                .toList();
    }

    @Transactional
    public void check(
            final Long itemId,
            final String ssaid
    ) {
        final TeamAssignedItem item = teamAssignedItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "담당"));
        validateOwner(ssaid, item);
        item.check();
    }

    @Transactional
    public void uncheck(
            final Long itemId,
            final String ssaid
    ) {
        final TeamAssignedItem item = teamAssignedItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "담당"));
        validateOwner(ssaid, item);
        item.uncheck();
    }

    private void validateDuplicateName(
            final Long teamMemberId,
            final String name
    ) {
        if (teamAssignedItemInfoRepository.existsByTeamBottariIdAndName(teamMemberId, name)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_EXISTS, "담당");
        }
    }

    private TeamAssignedItemInfo saveTeamAssignedItemInfo(
            final String name,
            final TeamBottari teamBottari
    ) {
        final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo(name, teamBottari);

        return teamAssignedItemInfoRepository.save(teamAssignedItemInfo);
    }

    public List<TeamMember> findAssignedTeamMembersByRequest(
            final List<String> teamMemberNames,
            final TeamBottari teamBottari
    ) {
        final List<TeamMember> targetTeamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottari.getId())
                .stream()
                .filter(member -> teamMemberNames.contains(member.getMember().getName()))
                .toList();
        if (targetTeamMembers.size() != teamMemberNames.size()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI);
        }

        return targetTeamMembers;
    }

    private void validateMemberInTeam(
            final Long bottariId,
            final String ssaid
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        if (!teamMemberRepository.existsByTeamBottariIdAndMemberId(bottariId, member.getId())) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI);
        }
    }

    private void saveAssignedItemToTeamMembers(
            final TeamAssignedItemInfo savedTeamAssignedItemInfo,
            final List<TeamMember> teamMembers
    ) {
        final List<TeamAssignedItem> teamAssignedItems = teamMembers.stream()
                .map(member -> new TeamAssignedItem(savedTeamAssignedItemInfo, member))
                .toList();
        teamAssignedItemRepository.saveAll(teamAssignedItems);
    }

    private Map<TeamAssignedItemInfo, List<TeamAssignedItem>> groupByInfo(final List<TeamAssignedItem> assignedItems) {
        return assignedItems.stream()
                .collect(Collectors.groupingBy(TeamAssignedItem::getInfo));
    }

    private TeamItemStatusResponse buildTeamStatusResponse(
            final Map<TeamAssignedItemInfo, List<TeamAssignedItem>> itemsByInfo,
            final TeamAssignedItemInfo info
    ) {
        final List<TeamAssignedItem> items = itemsByInfo.get(info);

        return TeamItemStatusResponse.of(
                info,
                items,
                countCheckedItem(items),
                items.size()
        );
    }

    private int countCheckedItem(final List<TeamAssignedItem> items) {
        final long count = items.stream()
                .filter(TeamAssignedItem::isChecked)
                .count();

        return Math.toIntExact(count);
    }

    private void validateOwner(
            final String ssaid,
            final TeamAssignedItem item
    ) {
        if (!item.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_OWNED, "본인의 팀 보따리 물품이 아닙니다.");
        }
    }
}
