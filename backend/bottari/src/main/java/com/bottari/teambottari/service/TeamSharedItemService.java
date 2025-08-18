package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.FcmMessageConverter;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.fcm.dto.MessageType;
import com.bottari.fcm.dto.SendMessageRequest;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.CreateTeamItemRequest;
import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.event.CheckTeamSharedItemEvent;
import com.bottari.teambottari.repository.TeamMemberRepository;
import com.bottari.teambottari.repository.TeamSharedItemInfoRepository;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamSharedItemService {

    private final FcmMessageSender fcmMessageSender;
    private final FcmMessageConverter fcmMessageConverter;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TeamSharedItemRepository teamSharedItemRepository;
    private final TeamSharedItemInfoRepository teamSharedItemInfoRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    private static Long memberIdByItem(final TeamSharedItem item) {
        return item.getTeamMember()
                .getMember()
                .getId();
    }

    public List<ReadSharedItemResponse> getAllByTeamBottariId(final Long teamBottariId) {
        final List<TeamSharedItemInfo> sharedItemInfos = teamSharedItemInfoRepository.findAllByTeamBottariId(
                teamBottariId
        );

        return sharedItemInfos.stream()
                .map(ReadSharedItemResponse::from)
                .toList();
    }

    @Transactional
    public Long create(
            final TeamMember teamMember,
            final CreateTeamItemRequest request
    ) {
        final TeamBottari teamBottari = teamMember.getTeamBottari();
        validateDuplicateName(teamBottari.getId(), request.name());
        final TeamSharedItemInfo savedTeamSharedItemInfo = saveTeamSharedItemInfo(request.name(), teamBottari);
        final List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottari.getId());
        saveSharedItemToTeamMembers(savedTeamSharedItemInfo, teamMembers);

        return savedTeamSharedItemInfo.getId();
    }

    @Transactional
    public void delete(
            final Long id,
            final String ssaid
    ) {
        final TeamSharedItemInfo teamSharedItemInfo = teamSharedItemInfoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "공통"));
        validateMemberInTeam(teamSharedItemInfo.getTeamBottari().getId(), ssaid);
        teamSharedItemRepository.deleteAllByInfo(teamSharedItemInfo);
        teamSharedItemInfoRepository.delete(teamSharedItemInfo);
    }

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
        final TeamSharedItem item = teamSharedItemRepository.findByIdWithTeamMember(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "공통"));
        validateOwner(ssaid, item);
        item.check();
        publishCheckEvent(item);
    }

    @Transactional
    public void uncheck(
            final Long itemId,
            final String ssaid
    ) {
        final TeamSharedItem item = teamSharedItemRepository.findByIdWithTeamMember(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "공통"));
        validateOwner(ssaid, item);
        item.uncheck();
        publishCheckEvent(item);
    }

    public void sendRemindAlarm(
            final Long infoId,
            final String ssaid
    ) {
        final TeamSharedItemInfo info = teamSharedItemInfoRepository.findById(infoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_INFO_NOT_FOUND, "공통"));
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        validateMemberInTeam(info.getTeamBottari(), member);
        final List<TeamSharedItem> items = teamSharedItemRepository.findAllByInfoIdWithMember(infoId);
        final List<Long> uncheckedMemberIds = collectUncheckedMemberIds(items);
        sendRemindMessageToMembers(info, uncheckedMemberIds);
    }

    private void validateDuplicateName(
            final Long teamMemberId,
            final String name
    ) {
        if (teamSharedItemInfoRepository.existsByTeamBottariIdAndName(teamMemberId, name)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_EXISTS, "공통");
        }
    }

    private TeamSharedItemInfo saveTeamSharedItemInfo(
            final String name,
            final TeamBottari teamBottari
    ) {
        final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo(name, teamBottari);

        return teamSharedItemInfoRepository.save(teamSharedItemInfo);
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

    private void saveSharedItemToTeamMembers(
            final TeamSharedItemInfo savedTeamSharedItemInfo,
            final List<TeamMember> teamMembers
    ) {
        final List<TeamSharedItem> teamSharedItems = teamMembers.stream()
                .map(member -> new TeamSharedItem(savedTeamSharedItemInfo, member))
                .toList();
        teamSharedItemRepository.saveAll(teamSharedItems);
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
                .map(TeamSharedItemService::memberIdByItem)
                .toList();
    }

    private void publishCheckEvent(final TeamSharedItem item) {
        final TeamMember teamMember = item.getTeamMember();
        final CheckTeamSharedItemEvent event = new CheckTeamSharedItemEvent(
                teamMember.getTeamBottari().getId(),
                teamMember.getMember().getId(),
                item.getInfo().getId(),
                item.getId(),
                item.isChecked()
        );
        applicationEventPublisher.publishEvent(event);
    }

    private void sendRemindMessageToMembers(
            final TeamSharedItemInfo info,
            final List<Long> uncheckedMemberIds
    ) {
        final SendMessageRequest sendMessageRequest = fcmMessageConverter.convert(info.getTeamBottari(), info,
                MessageType.REMIND_BY_ITEM);
        fcmMessageSender.sendMessageToMembers(uncheckedMemberIds, sendMessageRequest);
    }

    private void validateOwner(
            final String ssaid,
            final TeamSharedItem item
    ) {
        if (!item.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_OWNED, "본인의 팀 보따리 물품이 아닙니다.");
        }
    }

    private void validateMemberInTeam(
            final TeamBottari teamBottari,
            final Member member
    ) {
        if (!teamMemberRepository.existsByTeamBottariIdAndMemberId(teamBottari.getId(), member.getId())) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI);
        }
    }
}
