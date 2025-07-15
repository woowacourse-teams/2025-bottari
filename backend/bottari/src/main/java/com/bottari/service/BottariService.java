package com.bottari.service;

import com.bottari.domain.Bottari;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariRequest;
import com.bottari.repository.BottariRepository;
import com.bottari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariService {

    private final BottariRepository bottariRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(
            final String ssaid,
            final CreateBottariRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new IllegalArgumentException("해당 ssaid로 가입된 사용자가 없습니다."));
        final Bottari bottari = new Bottari(request.title(), member);
        final Bottari savedBottari = bottariRepository.save(bottari);

        return savedBottari.getId();
    }
}
