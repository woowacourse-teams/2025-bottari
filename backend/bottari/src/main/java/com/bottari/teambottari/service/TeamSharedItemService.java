package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamSharedItemService {

    private final TeamSharedItemRepository teamSharedItemRepository;

    public List<TeamItemStatusResponse> getAllWithMemberStatusByTeamBottariId(final Long teamBottariId) {
        final List<TeamSharedItem> items = teamSharedItemRepository.findAllByTeamBottariId(teamBottariId);
        final Map<TeamSharedItemInfo, List<TeamSharedItem>> itemsByInfo = groupByInfo(items);

        return itemsByInfo.keySet()
                .stream()
                .map(info -> TeamItemStatusResponse.of(info, itemsByInfo.get(info)))
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

    private Map<TeamSharedItemInfo, List<TeamSharedItem>> groupByInfo(final List<TeamSharedItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(TeamSharedItem::getInfo));
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
