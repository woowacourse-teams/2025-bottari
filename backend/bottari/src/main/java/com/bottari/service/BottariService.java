package com.bottari.service;

import com.bottari.domain.Alarm;
import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariRequest;
import com.bottari.dto.ReadBottariResponse;
import com.bottari.repository.AlarmRepository;
import com.bottari.repository.BottariItemRepository;
import com.bottari.repository.BottariRepository;
import com.bottari.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariService {

    private final BottariRepository bottariRepository;
    private final MemberRepository memberRepository;
    private final BottariItemRepository bottariItemRepository;
    private final AlarmRepository alarmRepository;

    public ReadBottariResponse getById(
            final String ssaid,
            final Long id
    ) {
        final Bottari bottari = bottariRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보따리를 찾을 수 없습니다."));
        validateOwner(ssaid, bottari);
        final List<BottariItem> bottariItems = bottariItemRepository.findAllByBottariId(id);
        final Alarm alarm = alarmRepository.findByBottariId(id)
                .orElse(null);

        return ReadBottariResponse.of(bottari, bottariItems, alarm);
    }

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

    private void validateOwner(
            final String ssaid,
            final Bottari bottari
    ) {
        if (!bottari.isOwner(ssaid)) {
            throw new IllegalArgumentException("본인의 보따리가 아닙니다.");
        }
    }
}
