package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.CreateTeamAssignedItemRequest;
import com.bottari.teambottari.dto.CreateTeamItemRequest;
import com.bottari.teambottari.dto.ReadAssignedItemResponse;
import com.bottari.teambottari.dto.ReadPersonalItemResponse;
import com.bottari.teambottari.dto.ReadSharedItemResponse;
import com.bottari.teambottari.dto.ReadTeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemStatusResponse;
import com.bottari.teambottari.dto.TeamItemTypeRequest;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.dto.UpdateAssignedItemRequest;
import com.bottari.teambottari.repository.TeamBottariRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamItemFacade {

    private final TeamSharedItemService teamSharedItemService;
    private final TeamAssignedItemService teamAssignedItemService;
    private final TeamPersonalItemService teamPersonalItemService;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamBottariRepository teamBottariRepository;
    private final MemberRepository memberRepository;

    public List<ReadSharedItemResponse> getSharedItems(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamBottari teamBottari = getTeamBottariById(teamBottariId);
        final Member member = getMemberBySsaid(ssaid);
        validateMemberInTeam(teamBottari.getId(), member);

        return teamSharedItemService.getAllByTeamBottariId(teamBottariId);
    }

    public List<ReadAssignedItemResponse> getAssignedItems(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamBottari teamBottari = getTeamBottariById(teamBottariId);
        final Member member = getMemberBySsaid(ssaid);
        validateMemberInTeam(teamBottari.getId(), member);

        return teamAssignedItemService.getAllByTeamBottariId(teamBottariId);
    }

    public List<ReadPersonalItemResponse> getPersonalItems(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamBottari teamBottari = getTeamBottariById(teamBottariId);
        final Member member = getMemberBySsaid(ssaid);
        validateMemberInTeam(teamBottari.getId(), member);

        return teamPersonalItemService.getAllByTeamBottariId(teamBottariId, member.getId());
    }

    @Transactional
    public Long createSharedItem(
            final Long teamBottariId,
            final CreateTeamItemRequest request,
            final String ssaid
    ) {
        validateTeamBottari(teamBottariId);
        final Member member = getMemberBySsaid(ssaid);
        validateMemberInTeam(teamBottariId, member);
        final TeamMember teamMember = getTeamMemberByTeamBottariIdAndSsaid(teamBottariId, ssaid);

        return teamSharedItemService.create(teamMember, request);
    }

    @Transactional
    public Long createAssignedItem(
            final Long teamBottariId,
            final CreateTeamAssignedItemRequest request,
            final String ssaid
    ) {
        validateTeamBottari(teamBottariId);
        final TeamMember teamMember = getTeamMemberByTeamBottariIdAndSsaid(teamBottariId, ssaid);

        return teamAssignedItemService.create(teamMember, request);
    }

    @Transactional
    public Long createPersonalItem(
            final Long teamBottariId,
            final CreateTeamItemRequest request,
            final String ssaid
    ) {
        validateTeamBottari(teamBottariId);
        final Member member = getMemberBySsaid(ssaid);
        validateMemberInTeam(teamBottariId, member);
        final TeamMember teamMember = getTeamMemberByTeamBottariIdAndSsaid(teamBottariId, ssaid);

        return teamPersonalItemService.create(teamMember, request);
    }

    @Transactional
    public void updateAssignedItem(
            final Long teamBottariId,
            final Long assignedItemId,
            final UpdateAssignedItemRequest request,
            final String ssaid
    ) {
        final TeamBottari teamBottari = getTeamBottariById(teamBottariId);
        final Member member = getMemberBySsaid(ssaid);
        validateMemberInTeam(teamBottari.getId(), member);
        teamAssignedItemService.update(teamBottariId, assignedItemId, request);
    }

    public void delete(
            final Long id,
            final String ssaid,
            final TeamItemTypeRequest request
    ) {
        switch (request.type()) {
            case SHARED -> teamSharedItemService.delete(id, ssaid);
            case ASSIGNED -> teamAssignedItemService.delete(id, ssaid);
            case PERSONAL -> teamPersonalItemService.delete(id, ssaid);
        }
    }

    public ReadTeamItemStatusResponse getTeamItemStatus(
            final Long teamBottariId,
            final String ssaid
    ) {
        validateTeamBottari(teamBottariId);
        final Member member = getMemberBySsaid(ssaid);
        validateMemberInTeam(teamBottariId, member);
        final List<TeamItemStatusResponse> sharedItemResponses = teamSharedItemService.getAllWithMemberStatusByTeamBottariId(
                teamBottariId);
        final List<TeamItemStatusResponse> assignedItemResponses = teamAssignedItemService.getAllWithMemberStatusByTeamBottariId(
                teamBottariId);

        return ReadTeamItemStatusResponse.of(sharedItemResponses, assignedItemResponses);
    }

    public TeamMemberChecklistResponse getCheckList(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamMember teamMember = getTeamMemberByTeamBottariIdAndSsaid(teamBottariId, ssaid);
        final List<TeamMemberItemResponse> teamSharedItems = teamSharedItemService.getAllByTeamMember(teamMember);
        final List<TeamMemberItemResponse> teamAssignedItems = teamAssignedItemService.getAllByTeamMember(teamMember);
        final List<TeamMemberItemResponse> teamPersonalItems = teamPersonalItemService.getAllByTeamMember(teamMember);

        return TeamMemberChecklistResponse.of(teamSharedItems, teamAssignedItems, teamPersonalItems);
    }

    public void check(
            final Long id,
            final String ssaid,
            final TeamItemTypeRequest request
    ) {
        switch (request.type()) {
            case SHARED -> teamSharedItemService.check(id, ssaid);
            case ASSIGNED -> teamAssignedItemService.check(id, ssaid);
            case PERSONAL -> teamPersonalItemService.check(id, ssaid);
        }
    }

    public void uncheck(
            final Long id,
            final String ssaid,
            final TeamItemTypeRequest request
    ) {
        switch (request.type()) {
            case SHARED -> teamSharedItemService.uncheck(id, ssaid);
            case ASSIGNED -> teamAssignedItemService.uncheck(id, ssaid);
            case PERSONAL -> teamPersonalItemService.uncheck(id, ssaid);
        }
    }

    public void sendRemindAlarmByInfo(
            final Long infoId,
            final TeamItemTypeRequest request,
            final String ssaid
    ) {
        switch (request.type()) {
            case SHARED -> teamSharedItemService.sendRemindAlarm(infoId, ssaid);
            case ASSIGNED -> teamAssignedItemService.sendRemindAlarm(infoId, ssaid);
            case PERSONAL -> throw new BusinessException(
                    ErrorCode.TEAM_BOTTARI_ITEM_INAPPROPRIATE_TYPE, "보채기 알람은 공통/담당 물품만 가능합니다.");
        }
    }

    private TeamBottari getTeamBottariById(final Long teamBottariId) {
        return teamBottariRepository.findById(teamBottariId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND));
    }

    private Member getMemberBySsaid(final String ssaid) {
        return memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
    }

    private TeamMember getTeamMemberByTeamBottariIdAndSsaid(
            final Long teamBottariId,
            final String ssaid
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));

        return teamMemberRepository.findByTeamBottariIdAndMemberId(teamBottariId, member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI));
    }

    private void validateTeamBottari(final Long teamBottariId) {
        if (!teamBottariRepository.existsById(teamBottariId)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND);
        }
    }

    private void validateMemberInTeam(
            final Long teamBottariId,
            final Member member
    ) {
        if (!teamMemberRepository.existsByTeamBottariIdAndMemberId(teamBottariId, member.getId())) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI);
        }
    }
}
