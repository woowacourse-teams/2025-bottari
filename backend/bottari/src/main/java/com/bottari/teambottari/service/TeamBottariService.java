package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.InviteCodeGenerator;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.repository.TeamBottariRepository;
import com.bottari.teambottari.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamBottariService {

    private final TeamBottariRepository teamBottariRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(
            final String ssaid,
            final CreateTeamBottariRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        try {
            final String inviteCode = InviteCodeGenerator.generate();
            final TeamBottari teamBottari = new TeamBottari(request.title(), member, inviteCode);
            final TeamBottari savedTeamBottari = teamBottariRepository.save(teamBottari);
            final TeamMember teamMember = new TeamMember(savedTeamBottari, member);
            teamMemberRepository.save(teamMember);

            return savedTeamBottari.getId();
        } catch (final DataIntegrityViolationException exception) {
            throw new BusinessException(ErrorCode.TEAM_BOTTARI_INVITE_CODE_GENERATION_FAILED);
        }
    }
}
