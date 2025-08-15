package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.JoinTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.dto.ReadTeamMemberStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import com.bottari.teambottari.repository.TeamAssignedItemRepository;
import com.bottari.teambottari.repository.TeamBottariRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import com.bottari.teambottari.repository.TeamSharedItemInfoRepository;
import com.bottari.teambottari.repository.TeamSharedItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamBottariRepository teamBottariRepository;
    private final TeamSharedItemRepository teamSharedItemRepository;
    private final TeamAssignedItemRepository teamAssignedItemRepository;
    private final TeamSharedItemInfoRepository teamSharedItemInfoRepository;
    private final MemberRepository memberRepository;

    private final FcmMessageSender fcmMessageSender;

    @Transactional(readOnly = true)
    public ReadTeamMemberInfoResponse getTeamMemberInfoByTeamBottariId(
            final Long teamBottariId,
            final String ssaid
    ) {
        final TeamBottari teamBottari = teamBottariRepository.findById(teamBottariId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND));
        final List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottariId);
        validateMemberInTeam(ssaid, teamMembers);

        return ReadTeamMemberInfoResponse.of(teamBottari, teamMembers);
    }

    @Transactional(readOnly = true)
    public List<ReadTeamMemberStatusResponse> getTeamMemberStatusByTeamBottariId(
            final Long teamBottariId,
            final String ssaid
    ) {
        validateTeamBottariExists(teamBottariId);
        final List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamBottariId(teamBottariId);
        validateMemberInTeam(ssaid, teamMembers);

        return teamMembers.stream()
                .map(this::buildReadTeamMemberStatusResponse)
                .toList();
    }

    @Transactional
    public Long joinTeamBottari(
            final JoinTeamBottariRequest request,
            final String ssaid
    ) {
        final TeamBottari teamBottari = teamBottariRepository.findByInviteCode(request.inviteCode())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.TEAM_BOTTARI_NOT_FOUND,
                        "해당하는 초대코드 없음"));
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        validateMemberNotInTeam(teamBottari, member);
        final TeamMember teamMember = new TeamMember(teamBottari, member);
        teamMemberRepository.save(teamMember);
        addTeamMemberSharedItems(teamBottari.getId(), teamMember);

        return teamMember.getId();
    }

    private void addTeamMemberSharedItems(
            final Long teamBottariId,
            final TeamMember teamMember
    ) {
        final List<TeamSharedItemInfo> sharedItemInfos = teamSharedItemInfoRepository.findAllByTeamBottariId(
                teamBottariId
        );
        final List<TeamSharedItem> teamSharedItems = sharedItemInfos.stream()
                .map(info -> new TeamSharedItem(info, teamMember))
                .toList();
        teamSharedItemRepository.saveAll(teamSharedItems);
    }

    public void sendRemindAlarm(
            final Long teamMemberId, // 보채기를 당할사람
            final String ssaid // 보채기를 누른사람
    ) {
        final Member reminder = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        final TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_MEMBER_NOT_FOUND));
        validateMemberInTeam(teamMember.getTeamBottari(), reminder);
    }

    private void validateMemberInTeam(
            final String ssaid,
            final List<TeamMember> teamMembers
    ) {
        teamMembers.stream()
                .filter(teamMember -> teamMember.getMember().isSameBySsaid(ssaid))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI));
    }

    private void validateMemberInTeam(
            final TeamBottari teamBottari,
            final Member member
    ) {
        if (!teamMemberRepository.existsByTeamBottariIdAndMemberId(teamBottari.getId(), member.getId())) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_IN_TEAM_BOTTARI);
        }
    }

    private void validateMemberNotInTeam(
            final TeamBottari teamBottari,
            final Member member
    ) {
        if (teamMemberRepository.existsByTeamBottariIdAndMemberId(teamBottari.getId(), member.getId())) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_IN_TEAM_BOTTARI);
        }
    }

    private void validateTeamBottariExists(final Long teamBottariId) {
        if (!teamBottariRepository.existsById(teamBottariId)) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_NOT_FOUND);
        }
    }

    private ReadTeamMemberStatusResponse buildReadTeamMemberStatusResponse(final TeamMember teamMember) {
        final List<TeamSharedItem> sharedItems = teamSharedItemRepository.findAllByTeamMemberId(teamMember.getId());
        final List<TeamAssignedItem> assignedItems = teamAssignedItemRepository.findAllByTeamMemberId(
                teamMember.getId());
        final int totalItemsCount = sharedItems.size() + assignedItems.size();
        final List<TeamMemberItemResponse> sharedItemResponses = sharedItems.stream()
                .map(TeamMemberItemResponse::from)
                .toList();
        final List<TeamMemberItemResponse> assignedItemResponses = assignedItems.stream()
                .map(TeamMemberItemResponse::from)
                .toList();

        return new ReadTeamMemberStatusResponse(
                teamMember.getMember().getId(),
                teamMember.getMember().getName(),
                teamMember.isTeamBottariOwner(),
                totalItemsCount,
                calculateCheckedItemsCount(sharedItems, assignedItems),
                sharedItemResponses,
                assignedItemResponses
        );
    }

    private int calculateCheckedItemsCount(
            final List<TeamSharedItem> sharedItems,
            final List<TeamAssignedItem> assignedItems
    ) {
        final int checkedSharedItemsCount = (int) sharedItems.stream()
                .filter(TeamSharedItem::isChecked)
                .count();
        final int checkedAssignedItemsCount = (int) assignedItems.stream()
                .filter(TeamAssignedItem::isChecked)
                .count();

        return checkedSharedItemsCount + checkedAssignedItemsCount;
    }
}
