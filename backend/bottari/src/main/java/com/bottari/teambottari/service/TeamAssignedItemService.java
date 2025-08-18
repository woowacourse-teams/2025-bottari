package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.FcmMessageConverter;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.fcm.dto.MessageType;
import com.bottari.fcm.dto.SendMessageRequest;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.CreateTeamAssignedItemRequest;
import com.bottari.teambottari.dto.ReadAssignedItemResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.event.CheckTeamAssignedItemEvent;
import com.bottari.teambottari.event.CreateAssignedItemEvent;
import com.bottari.teambottari.event.DeleteAssignedItemEvent;
import com.bottari.teambottari.repository.TeamAssignedItemInfoRepository;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamAssignedItemService {

    private final TeamAssignedItemRepository teamAssignedItemRepository;
    private final TeamAssignedItemInfoRepository teamAssignedItemInfoRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    private final FcmMessageSender fcmMessageSender;
    private final FcmMessageConverter fcmMessageConverter;

    private final ApplicationEventPublisher applicationEventPublisher;

    public List<ReadAssignedItemResponse> getAllByTeamBottariId(final Long teamBottariId) {
        final List<TeamAssignedItem> assignedItems = teamAssignedItemRepository.findAllByTeamBottariId(teamBottariId);
        final Map<TeamAssignedItemInfo, List<Member>> assigneesByInfo = groupMembersByAssignedItemInfo(assignedItems);

        return assigneesByInfo.entrySet()
                .stream()
                .map(entry -> {
                    final TeamAssignedItemInfo teamAssignedItemInfo = entry.getKey();
                    final List<Member> assignees = entry.getValue();

                    return ReadAssignedItemResponse.from(teamAssignedItemInfo, assignees);
                })
                .toList();
    }

    @Transactional
    public Long create(
            final TeamMember teamMember,
            final CreateTeamAssignedItemRequest request
    ) {
        final TeamBottari teamBottari = teamMember.getTeamBottari();
        validateDuplicateName(teamBottari.getId(), request.name());
        final List<String> requestAssignedMemberNames = getRequestAssignedMemberNames(request.memberIds());
        final List<TeamMember> teamMembers = getAssignedTeamMembersByRequest(requestAssignedMemberNames, teamBottari);
        final TeamAssignedItemInfo savedTeamAssignedItemInfo = saveTeamAssignedItemInfo(request.name(), teamBottari);
        saveAssignedItemToTeamMembers(savedTeamAssignedItemInfo, teamMembers);
        applicationEventPublisher.publishEvent(new CreateAssignedItemEvent(
                teamBottari.getId(),
                savedTeamAssignedItemInfo.getId(),
                savedTeamAssignedItemInfo.getName(),
                request.memberIds()
        ));

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
        applicationEventPublisher.publishEvent(new DeleteAssignedItemEvent(
                teamAssignedItemInfo.getTeamBottari().getId(),
                id,
                teamAssignedItemInfo.getName()
        ));
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
        final TeamAssignedItem item = teamAssignedItemRepository.findByIdWithTeamMember(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "담당"));
        validateOwner(ssaid, item);
        item.check();
        publishCheckEvent(item);
    }

    @Transactional
    public void uncheck(
            final Long itemId,
            final String ssaid
    ) {
        final TeamAssignedItem item = teamAssignedItemRepository.findByIdWithTeamMember(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NOT_FOUND, "담당"));
        validateOwner(ssaid, item);
        item.uncheck();
        publishCheckEvent(item);
    }

    private Map<TeamAssignedItemInfo, List<Member>> groupMembersByAssignedItemInfo(final List<TeamAssignedItem> assignedItems) {
        return assignedItems.stream()
                .collect(Collectors.groupingBy(
                        TeamAssignedItem::getInfo,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                teamAssignedItem -> teamAssignedItem.getTeamMember().getMember(),
                                Collectors.toList()
                        )
                ));
    }

    private void validateDuplicateName(
            final Long teamBottariId,
            final String name
    ) {
        if (teamAssignedItemInfoRepository.existsByTeamBottariIdAndName(teamBottariId, name)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_ALREADY_EXISTS, "담당");
        }
    }

    private List<String> getRequestAssignedMemberNames(final List<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_NO_ASSIGNED_MEMBERS);
        }
        final List<Member> members = memberRepository.findAllById(memberIds);
        if (members.size() != memberIds.size()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "요청된 팀원 중 일부가 존재하지 않습니다.");
        }

        return members.stream()
                .map(Member::getName)
                .toList();
    }

    private TeamAssignedItemInfo saveTeamAssignedItemInfo(
            final String name,
            final TeamBottari teamBottari
    ) {
        final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo(name, teamBottari);

        return teamAssignedItemInfoRepository.save(teamAssignedItemInfo);
    }

    private List<TeamMember> getAssignedTeamMembersByRequest(
            final List<String> teamMemberNames,
            final TeamBottari teamBottari
    ) {
        final List<TeamMember> allTeamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottari.getId());
        final Set<String> requestNames = new HashSet<>(teamMemberNames);
        final Set<String> allTeamMemberNames = allTeamMembers.stream()
                .map(m -> m.getMember().getName())
                .collect(Collectors.toSet());
        if (!allTeamMemberNames.containsAll(requestNames)) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI, "요청된 팀원 중 일부가 팀에 속해 있지 않습니다.");
        }

        return allTeamMembers.stream()
                .filter(member -> requestNames.contains(member.getMember().getName()))
                .toList();
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

    private void publishCheckEvent(final TeamAssignedItem item) {
        final TeamMember teamMember = item.getTeamMember();
        final CheckTeamAssignedItemEvent event = new CheckTeamAssignedItemEvent(
                teamMember.getTeamBottari().getId(),
                teamMember.getMember().getId(),
                item.getInfo().getId(),
                item.getId(),
                item.isChecked()
        );
        applicationEventPublisher.publishEvent(event);
    }

    public void sendRemindAlarm(
            final Long infoId,
            final String ssaid
    ) {
        final TeamAssignedItemInfo info = teamAssignedItemInfoRepository.findById(infoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_ITEM_INFO_NOT_FOUND, "담당"));
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        validateMemberInTeam(info.getTeamBottari(), member);
        final List<TeamAssignedItem> items = teamAssignedItemRepository.findAllByInfoIdWithMember(infoId);
        final List<Long> uncheckedMemberIds = collectUncheckedMemberIds(items);
        sendRemindMessageToMembers(info, uncheckedMemberIds);
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
                .map(this::memberIdByItem)
                .toList();
    }

    private Long memberIdByItem(final TeamAssignedItem item) {
        return item.getTeamMember()
                .getMember()
                .getId();
    }

    private void sendRemindMessageToMembers(
            final TeamAssignedItemInfo info,
            final List<Long> uncheckedMemberIds
    ) {
        final SendMessageRequest sendMessageRequest = fcmMessageConverter.convert(
                info.getTeamBottari(),
                info,
                MessageType.REMIND_BY_ITEM
        );
        fcmMessageSender.sendMessageToMembers(uncheckedMemberIds, sendMessageRequest);
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
            final Member member
    ) {
        if (!teamMemberRepository.existsByTeamBottariIdAndMemberId(teamBottari.getId(), member.getId())) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI);
        }
    }
}
