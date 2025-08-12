package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamAssignedItemService {

    private final TeamAssignedItemRepository teamAssignedItemRepository;

    public List<TeamAssignedItem> getAllByTeamMember(final TeamMember teamMember) {
        return teamAssignedItemRepository.findAllByTeamMemberId(teamMember.getId());
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

    private void validateOwner(
            final String ssaid,
            final TeamAssignedItem item
    ) {
        if (!item.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_OWNED, "본인의 팀 보따리 물품이 아닙니다.");
        }
    }
}
