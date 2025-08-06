package com.bottari.bottari.service;

import com.bottari.alarm.domain.Alarm;
import com.bottari.bottari.domain.Bottari;
import com.bottari.bottari.domain.BottariItem;
import com.bottari.bottari.repository.BottariItemRepository;
import com.bottari.bottari.repository.BottariRepository;
import com.bottari.member.domain.Member;
import com.bottari.alarm.dto.AlarmResponse;
import com.bottari.bottari.dto.CreateBottariRequest;
import com.bottari.bottari.dto.ReadBottariPreviewResponse;
import com.bottari.bottari.dto.ReadBottariResponse;
import com.bottari.bottari.dto.UpdateBottariRequest;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.alarm.repository.AlarmRepository;
import com.bottari.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
        final Bottari bottari = bottariRepository.findByIdWithMember(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_NOT_FOUND));
        validateOwner(ssaid, bottari);
        final List<BottariItem> bottariItems = bottariItemRepository.findAllByBottariId(id);
        final Alarm alarm = alarmRepository.findByBottariId(id)
                .orElse(null);

        return ReadBottariResponse.of(bottari, bottariItems, alarm);
    }

    public List<ReadBottariPreviewResponse> getAllBySsaidSortedByLatest(final String ssaid) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final List<Bottari> bottaries = bottariRepository.findAllByMemberIdOrderByCreatedAtDesc(member.getId());

        return buildReadBottariPreviewResponses(bottaries);
    }

    @Transactional
    public Long create(
            final String ssaid,
            final CreateBottariRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final Bottari bottari = new Bottari(request.title(), member);
        final Bottari savedBottari = bottariRepository.save(bottari);

        return savedBottari.getId();
    }

    @Transactional
    public void update(
            final UpdateBottariRequest request,
            final Long id,
            final String ssaid
    ) {
        final Bottari bottari = bottariRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_NOT_FOUND));
        validateOwner(ssaid, bottari);
        bottari.updateTitle(request.title());
    }

    @Transactional
    public void deleteById(
            final Long id,
            final String ssaid
    ) {
        final Bottari bottari = bottariRepository.findByIdWithMember(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_NOT_FOUND));
        validateOwner(ssaid, bottari);
        bottariItemRepository.deleteByBottariId(id);
        alarmRepository.deleteByBottariId(id);
        bottariRepository.deleteById(id);
    }

    private List<ReadBottariPreviewResponse> buildReadBottariPreviewResponses(final List<Bottari> bottaries) {
        final Map<Bottari, List<BottariItem>> bottariItemsGroupByBottari = groupingBottariItemsById(bottaries);
        final Map<Bottari, Alarm> alarmMap = groupingAlarmByBottari(bottaries);
        final List<ReadBottariPreviewResponse> readBottariPreviewResponses = new ArrayList<>();
        for (final Bottari bottari : bottaries) {
            final List<BottariItem> items = bottariItemsGroupByBottari.getOrDefault(bottari, Collections.emptyList());
            final ReadBottariPreviewResponse response = new ReadBottariPreviewResponse(
                    bottari.getId(),
                    bottari.getTitle(),
                    items.size(),
                    countCheckedItems(items),
                    AlarmResponse.from(alarmMap.getOrDefault(bottari, null))
            );
            readBottariPreviewResponses.add(response);
        }

        return readBottariPreviewResponses;
    }

    private int countCheckedItems(final List<BottariItem> items) {
        return (int) items.stream()
                .filter(BottariItem::isChecked)
                .count();
    }

    private Map<Bottari, Alarm> groupingAlarmByBottari(final List<Bottari> bottaries) {
        final List<Alarm> alarms = alarmRepository.findAllByBottariIn(bottaries);

        return alarms.stream()
                .collect(Collectors.toMap(
                                Alarm::getBottari,
                                Function.identity()
                        )
                );
    }

    private Map<Bottari, List<BottariItem>> groupingBottariItemsById(final List<Bottari> bottaries) {
        final List<BottariItem> bottariItems = bottariItemRepository.findAllByBottariIn(bottaries);

        return bottariItems.stream()
                .collect(Collectors.groupingBy(BottariItem::getBottari));
    }

    private void validateOwner(
            final String ssaid,
            final Bottari bottari
    ) {
        if (!bottari.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.BOTTARI_NOT_OWNED, "본인의 보따리가 아닙니다.");
        }
    }
}
