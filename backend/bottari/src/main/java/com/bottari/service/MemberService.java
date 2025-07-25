package com.bottari.service;

import com.bottari.domain.Member;
import com.bottari.dto.CheckRegistrationResponse;
import com.bottari.dto.CreateMemberRequest;
import com.bottari.dto.UpdateMemberRequest;
import com.bottari.repository.MemberRepository;
import java.util.Optional;
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

    public CheckRegistrationResponse checkRegistration(final String ssaid) {
        final Optional<Member> optionalMember = memberRepository.findBySsaid(ssaid);
        if (optionalMember.isPresent()) {
            final Member member = optionalMember.get();

            return new CheckRegistrationResponse(true, member.getName());
        }

        return new CheckRegistrationResponse(false, null);
    }

    @Transactional
    public void updateName(
            final String ssaid,
            final UpdateMemberRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new IllegalArgumentException("해당 ssaid로 가입된 사용자가 없습니다."));
        validateDuplicateName(request);
        member.updateName(request.name());
    }

    private void validateDuplicateName(final UpdateMemberRequest request) {
        if (memberRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 사용 중인 이름입니다.");
        }
    }
}
