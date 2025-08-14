package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.fcm.dto.MessageType;
import com.bottari.fcm.dto.SendMessageRequest;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.repository.TeamSharedItemInfoRepository;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamSharedItemService {

    private final FcmMessageSender fcmMessageSender;
    private final TeamSharedItemRepository teamSharedItemRepository;
    private final TeamSharedItemInfoRepository teamSharedItemInfoRepository;

    public List<TeamItemStatusResponse> getAllWithMemberStatusByTeamBottariId(final Long teamBottariId) {
        final List<TeamSharedItem> items = teamSharedItemRepository.findAllByTeamBottariId(teamBottariId);
        final Map<TeamSharedItemInfo, List<TeamSharedItem>> itemsByInfo = groupByInfo(items);

        return itemsByInfo.keySet()
                .stream()
                .sorted(Comparator.comparing(TeamSharedItemInfo::getCreatedAt))
                .map(info -> buildTeamStatusResponse(itemsByInfo, info))
                .toList();
    }

    public List<TeamMemberItemResponse> getAllByTeamMember(final TeamMember teamMember) {
        final List<TeamSharedItem> items = teamSharedItemRepository.findAllByTeamMemberId(teamMember.getId());

        return items.stream()
                .map(TeamMemberItemResponse::from)
                .toList();
    }

    @Transactional
    public void check(
            final Long itemId,
            final String ssaid
    ) {
        final TeamSharedItem item = teamSharedItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "공통"));
        validateOwner(ssaid, item);
        item.check();
    }

    @Transactional
    public void uncheck(
            final Long itemId,
            final String ssaid
    ) {
        final TeamSharedItem item = teamSharedItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "공통"));
        validateOwner(ssaid, item);
        item.uncheck();
    }

    public void sendRemindAlarm(final Long infoId) {
        final TeamSharedItemInfo info = teamSharedItemInfoRepository.findById(infoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_INFO_NOT_FOUND, "공통"));
        final List<TeamSharedItem> items = teamSharedItemRepository.findAllByInfoIdWithMember(infoId);
        final List<Long> uncheckedMemberIds = collectUncheckedMemberIds(items);
        final SendMessageRequest sendMessageRequest = SendMessageRequest.of(info.getTeamBottari(), MessageType.REMIND);
        fcmMessageSender.sendMessageToMembers(uncheckedMemberIds, sendMessageRequest);
    }

    private Map<TeamSharedItemInfo, List<TeamSharedItem>> groupByInfo(final List<TeamSharedItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(TeamSharedItem::getInfo));
    }

    private TeamItemStatusResponse buildTeamStatusResponse(
            final Map<TeamSharedItemInfo, List<TeamSharedItem>> itemsByInfo,
            final TeamSharedItemInfo info
    ) {
        final List<TeamSharedItem> items = itemsByInfo.get(info);

        return TeamItemStatusResponse.of(
                info,
                items,
                countCheckedItem(items),
                items.size()
        );
    }

    private int countCheckedItem(final List<TeamSharedItem> items) {
        final long count = items.stream()
                .filter(TeamSharedItem::isChecked)
                .count();

        return Math.toIntExact(count);
    }

    private List<Long> collectUncheckedMemberIds(final List<TeamSharedItem> items) {
        return items.stream()
                .filter(item -> !item.isChecked())
                .map(item -> item.getTeamMember().getMember().getId())
                .toList();
    }

    private void validateOwner(
            final String ssaid,
            final TeamSharedItem item
    ) {
        if (!item.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_OWNED, "본인의 팀 보따리 물품이 아닙니다.");
        }
    }
}
