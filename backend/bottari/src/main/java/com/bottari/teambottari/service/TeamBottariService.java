package com.bottari.teambottari.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.teambottari.domain.InviteCodeGenerator;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.repository.TeamBottariRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamBottariService {

    private final TeamBottariRepository teamBottariRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(
            final String ssaid,
            final CreateTeamBottariRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final String inviteCode = InviteCodeGenerator.generate();
        final TeamBottari teamBottari = new TeamBottari(request.title(), member, inviteCode);
        final TeamBottari savedTeamBottari = teamBottariRepository.save(teamBottari);

        return savedTeamBottari.getId();
    }
}
