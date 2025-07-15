package com.bottari.service;

import com.bottari.domain.Member;
import com.bottari.dto.CreateMemberRequest;
import com.bottari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long create(final CreateMemberRequest request) {
        final Member member = request.toMember();
        final Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }
}
