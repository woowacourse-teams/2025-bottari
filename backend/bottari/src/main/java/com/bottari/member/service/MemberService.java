package com.bottari.member.service;

import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import com.bottari.member.dto.CheckRegistrationResponse;
import com.bottari.member.dto.CreateMemberRequest;
import com.bottari.member.dto.UpdateMemberRequest;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String[] NAME_POOL = {
            "체커", "잊꾸", "짐장인", "출근러", "여행러",
            "물건러", "챙김러", "잊꾸러기", "보따리꾼", "가방요정"
    };

    private final MemberRepository memberRepository;

    @Transactional
    @Retryable(
            retryFor = DataIntegrityViolationException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 300, multiplier = 2)
    )
    public Long create(final CreateMemberRequest request) {
        validateDuplicateSsaid(request.ssaid());
        final String generatedName = generatedRandomCandidateName();
        final Member member = new Member(request.ssaid(), generatedName);
        final Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    @Recover
    public Long recoverCreate(
            final DataIntegrityViolationException exception,
            final CreateMemberRequest request
    ) {
        throw new BusinessException(ErrorCode.MEMBER_NAME_GENERATION_FAILED);
    }

    public CheckRegistrationResponse checkRegistration(final String ssaid) {
        final Optional<Member> optionalMember = memberRepository.findBySsaid(ssaid);
        if (optionalMember.isPresent()) {
            final Member member = optionalMember.get();

            return new CheckRegistrationResponse(true, member.getId(), member.getName());
        }

        return new CheckRegistrationResponse(false, null, null);
    }

    @Transactional
    public void updateName(
            final String ssaid,
            final UpdateMemberRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        validateDuplicateName(request);
        member.updateName(request.name());
    }

    private void validateDuplicateSsaid(final String ssaid) {
        if (memberRepository.existsBySsaid(ssaid)) {
            throw new BusinessException(ErrorCode.MEMBER_SSAID_ALREADY_EXISTS);
        }
    }

    private String generatedRandomCandidateName() {
        final String word = NAME_POOL[ThreadLocalRandom.current().nextInt(NAME_POOL.length)];
        final int suffix = ThreadLocalRandom.current().nextInt(10_000);

        return "%s-%04d".formatted(word, suffix);
    }

    private void validateDuplicateName(final UpdateMemberRequest request) {
        if (memberRepository.existsByName(request.name())) {
            throw new BusinessException(ErrorCode.MEMBER_NAME_ALREADY_EXISTS);
        }
    }
}
