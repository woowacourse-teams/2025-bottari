package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.fcm.dto.MessageType;
import com.bottari.fcm.dto.SendMessageRequest;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
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

    private final FcmMessageSender fcmMessageSender;
    private final TeamAssignedItemRepository teamAssignedItemRepository;
    private final TeamAssignedItemInfoRepository teamAssignedItemInfoRepository;
    private final TeamMemberRepository teamMemberRepository;

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

    public void sendRemindAlarm(
            final Long infoId,
            final String ssaid
    ) {
        final TeamAssignedItemInfo info = teamAssignedItemInfoRepository.findById(infoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_INFO_NOT_FOUND, "담당"));
        validateMemberInTeam(info.getTeamBottari(), ssaid);
        final List<TeamAssignedItem> items = teamAssignedItemRepository.findAllByInfoIdWithMember(infoId);
        final List<Long> uncheckedMemberIds = collectUncheckedMemberIds(items);
        final SendMessageRequest sendMessageRequest = SendMessageRequest.of(info.getTeamBottari(), MessageType.REMIND);
        fcmMessageSender.sendMessageToMembers(uncheckedMemberIds, sendMessageRequest);
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

    private List<Long> collectUncheckedMemberIds(final List<TeamAssignedItem> items) {
        return items.stream()
                .filter(item -> !item.isChecked())
                .map(item -> item.getTeamMember().getMember().getId())
                .toList();
    }

    private void validateOwner(
            final String ssaid,
            final TeamAssignedItem item
    ) {
        if (!item.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_OWNED, "본인의 팀 보따리 물품이 아닙니다.");
        }
    }

    private void validateMemberInTeam(
            final TeamBottari teamBottari,
            final String ssaid
    ) {
        if (!teamMemberRepository.existsByTeamBottariIdAndMemberSsaid(teamBottari.getId(), ssaid)) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI);
        }
    }
}
