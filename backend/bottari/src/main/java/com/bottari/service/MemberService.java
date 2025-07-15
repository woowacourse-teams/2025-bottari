package com.bottari.service;

import com.bottari.domain.Member;
import com.bottari.dto.CreateMemberRequest;
import com.bottari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long create(final CreateMemberRequest request) {
        validateDuplicateSsaid(request.ssaid());
        final Member member = request.toMember();
        final Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    private void validateDuplicateSsaid(final String ssaid) {
        if (memberRepository.existsBySsaid(ssaid)) {
            throw new IllegalArgumentException("중복된 ssaid입니다.");
        }
    }
}
