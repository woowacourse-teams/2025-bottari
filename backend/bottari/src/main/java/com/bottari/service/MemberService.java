package com.bottari.service;

import com.bottari.domain.Member;
import com.bottari.dto.CheckRegistrationResponse;
import com.bottari.dto.CreateMemberRequest;
import com.bottari.dto.UpdateMemberRequest;
import com.bottari.repository.MemberRepository;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String[] NAME_POOL = {
            "체커", "잊꾸", "짐장인", "출근러", "여행러",
            "물건러", "챙김러", "잊꾸러기", "보따리꾼", "가방요정"
    };
    private static final double MAX_TEMP_NAME_GENERATION_ATTEMPTS = 3;

    private final MemberRepository memberRepository;

    @Transactional
    public Long create(final CreateMemberRequest request) {
        validateDuplicateSsaid(request.ssaid());
        final Member savedMember = saveWithUniqueTempName(request.ssaid());

        return savedMember.getId();
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

    private void validateDuplicateSsaid(final String ssaid) {
        if (memberRepository.existsBySsaid(ssaid)) {
            throw new IllegalArgumentException("중복된 ssaid입니다.");
        }
    }

    private void validateDuplicateName(final UpdateMemberRequest request) {
        if (memberRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 사용 중인 이름입니다.");
        }
    }

    private Member saveWithUniqueTempName(final String ssaid) {
        for (int i = 0; i < MAX_TEMP_NAME_GENERATION_ATTEMPTS; i++) {
            try {
                final String tempName = createRandomNameCandidate();
                final Member member = new Member(ssaid, tempName);
                return memberRepository.save(member);
            } catch (DataIntegrityViolationException e) {
                if (i == MAX_TEMP_NAME_GENERATION_ATTEMPTS - 1) {
                    throw new IllegalStateException("고유한 임시 닉네임을 생성하고 저장하는 데 실패했습니다. (관리자 문의 필요)");
                }
            }
        }
        throw new IllegalStateException("알 수 없는 이유로 닉네임 생성에 실패했습니다.");
    }

    private String createRandomNameCandidate() {
        String word = NAME_POOL[ThreadLocalRandom.current().nextInt(NAME_POOL.length)];
        int suffix = ThreadLocalRandom.current().nextInt(10_000);
        return "%s-%04d".formatted(word, suffix);
    }
}
