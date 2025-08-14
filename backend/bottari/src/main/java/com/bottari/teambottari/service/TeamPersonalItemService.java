package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.dto.CreatePersonalItemRequest;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.repository.TeamPersonalItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamPersonalItemService {

    private final TeamPersonalItemRepository teamPersonalItemRepository;

    @Transactional
    public Long create(
            final TeamMember teamMember,
            final CreatePersonalItemRequest request
    ) {
        validateDuplicateName(teamMember.getId(), request.name());
        final TeamPersonalItem item = new TeamPersonalItem(request.name(), teamMember);
        final TeamPersonalItem savedTeamPersonalItem = teamPersonalItemRepository.save(item);

        return savedTeamPersonalItem.getId();
    }

    public void delete(
            final Long id,
            final String ssaid
    ) {
        final TeamPersonalItem item = teamPersonalItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "개인"));
        validateOwner(ssaid, item);
        teamPersonalItemRepository.delete(item);
    }

    public List<TeamMemberItemResponse> getAllByTeamMember(final TeamMember teamMember) {
        final List<TeamPersonalItem> items = teamPersonalItemRepository.findAllByTeamMemberId(teamMember.getId());

        return items.stream()
                .map(TeamMemberItemResponse::from)
                .toList();
    }

    @Transactional
    public void check(
            final Long itemId,
            final String ssaid
    ) {
        final TeamPersonalItem item = teamPersonalItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "개인"));
        validateOwner(ssaid, item);
        item.check();
    }

    @Transactional
    public void uncheck(
            final Long itemId,
            final String ssaid
    ) {
        final TeamPersonalItem item = teamPersonalItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "개인"));
        validateOwner(ssaid, item);
        item.uncheck();
    }

    private void validateDuplicateName(
            final Long teamMemberId,
            final String name
    ) {
        if (teamPersonalItemRepository.existsByTeamMemberIdAndName(teamMemberId, name)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_EXISTS, "개인");
        }
    }

    private void validateOwner(
            final String ssaid,
            final TeamPersonalItem item
    ) {
        if (!item.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_OWNED, "본인의 팀 보따리 물품이 아닙니다.");
        }
    }
}
