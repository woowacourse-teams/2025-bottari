package com.bottari.service;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateHistory;
import com.bottari.domain.BottariTemplateCursor;
import com.bottari.domain.BottariTemplateItem;
import com.bottari.domain.Member;
import com.bottari.domain.SortProperty;
import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.dto.ReadNextBottariTemplateRequest;
import com.bottari.dto.ReadNextBottariTemplateResponse;
import com.bottari.repository.BottariItemRepository;
import com.bottari.repository.BottariRepository;
import com.bottari.repository.BottariTemplateHistoryRepository;
import com.bottari.repository.BottariTemplateItemRepository;
import com.bottari.repository.BottariTemplateRepository;
import com.bottari.repository.MemberRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariTemplateService {

    private final BottariTemplateRepository bottariTemplateRepository;
    private final BottariTemplateItemRepository bottariTemplateItemRepository;
    private final BottariTemplateHistoryRepository bottariTemplateHistoryRepository;
    private final BottariRepository bottariRepository;
    private final BottariItemRepository bottariItemRepository;
    private final MemberRepository memberRepository;

    public ReadBottariTemplateResponse getById(final Long id) {
        final BottariTemplate bottariTemplate = bottariTemplateRepository.findByIdWithMember(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND));
        final List<BottariTemplateItem> bottariTemplateItems =
                bottariTemplateItemRepository.findAllByBottariTemplateId(bottariTemplate.getId());

        return ReadBottariTemplateResponse.of(bottariTemplate, bottariTemplateItems);
    }

    public List<ReadBottariTemplateResponse> getBySsaid(final String ssaid) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final List<BottariTemplate> bottariTemplateItems =
                bottariTemplateRepository.findAllByMemberIdWithMember(member.getId());
        final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate =
                groupingItemsByTemplate(bottariTemplateItems);

        return buildReadBottariTemplateResponses(itemsGroupByTemplate);
    }

    public List<ReadBottariTemplateResponse> getAll(final String query) {
        final List<BottariTemplate> bottariTemplates = bottariTemplateRepository.findAllWithMember(query);
        final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate = groupingItemsByTemplate(
                bottariTemplates);

        return buildReadBottariTemplateResponses(itemsGroupByTemplate);
    }

    public ReadNextBottariTemplateResponse getNextAll(final ReadNextBottariTemplateRequest request) {
        final BottariTemplateCursor cursor = request.toCursor();
        final Pageable pageable = cursor.toPageable();
        final Slice<BottariTemplate> bottariTemplates = getNextBySortProperty(cursor, pageable);
        final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate = groupingItemsByTemplate(
                bottariTemplates.getContent());
        final List<ReadBottariTemplateResponse> responses = buildReadBottariTemplateResponses(itemsGroupByTemplate);

        return ReadNextBottariTemplateResponse.of(
                new SliceImpl<>(responses, pageable, bottariTemplates.hasNext()), cursor.property());
    }

    @Transactional
    public Long create(
            final String ssaid,
            final CreateBottariTemplateRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final BottariTemplate bottariTemplate = new BottariTemplate(request.title(), member);
        final BottariTemplate savedBottariTemplate = bottariTemplateRepository.save(bottariTemplate);
        validateDuplicateItemNames(request.bottariTemplateItems());
        final List<BottariTemplateItem> bottariTemplateItems = request.bottariTemplateItems().stream()
                .map(name -> new BottariTemplateItem(name, savedBottariTemplate))
                .toList();
        bottariTemplateItemRepository.saveAll(bottariTemplateItems);

        return savedBottariTemplate.getId();
    }

    @Transactional
    public Long createBottari(
            final Long id,
            final String ssaid
    ) {
        final BottariTemplate bottariTemplate = bottariTemplateRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND));
        final List<BottariTemplateItem> bottariTemplateItems =
                bottariTemplateItemRepository.findAllByBottariTemplateId(id);
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final Bottari bottari = new Bottari(bottariTemplate.getTitle(), member);
        final Bottari savedBottari = bottariRepository.save(bottari);
        final List<BottariItem> bottariItems = bottariTemplateItems.stream()
                .map(item -> new BottariItem(item.getName(), bottari))
                .toList();
        bottariItemRepository.saveAll(bottariItems);
        increaseTakenCount(bottariTemplate, member);

        return savedBottari.getId();
    }

    @Transactional
    public void deleteById(
            final Long id,
            final String ssaid
    ) {
        final BottariTemplate bottariTemplate = bottariTemplateRepository.findByIdWithMember(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND));
        validateOwner(ssaid, bottariTemplate);
        bottariTemplateItemRepository.deleteByBottariTemplateId(id);
        bottariTemplateRepository.deleteById(id);
    }

    private Slice<BottariTemplate> getNextBySortProperty(
            final BottariTemplateCursor cursor,
            final Pageable pageable
    ) {
        final SortProperty property = SortProperty.fromProperty(cursor.property());
        return switch (property) {
            case SortProperty.CREATED_AT -> bottariTemplateRepository.findNextByCreatedAt(
                    cursor.query(), cursor.getCreatedAt(), cursor.lastId(), pageable);
            case SortProperty.TAKEN_COUNT -> bottariTemplateRepository.findNextByTakenCount(
                    cursor.query(), cursor.getTakenCount(), cursor.lastId(), pageable);
        };
    }

    private Map<BottariTemplate, List<BottariTemplateItem>> groupingItemsByTemplate(final List<BottariTemplate> bottariTemplates) {
        final Map<BottariTemplate, List<BottariTemplateItem>> groupByTemplates = new LinkedHashMap<>();
        final List<BottariTemplateItem> items = bottariTemplateItemRepository.findAllByBottariTemplateIn(
                bottariTemplates);
        for (final BottariTemplate bottariTemplate : bottariTemplates) {
            groupByTemplates.put(bottariTemplate, new ArrayList<>());
        }
        for (final BottariTemplateItem item : items) {
            final BottariTemplate bottariTemplate = item.getBottariTemplate();
            groupByTemplates.get(bottariTemplate).add(item);
        }

        return groupByTemplates;
    }

    private List<ReadBottariTemplateResponse> buildReadBottariTemplateResponses(
            final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate
    ) {
        final List<ReadBottariTemplateResponse> responses = new ArrayList<>();
        for (final BottariTemplate bottariTemplate : itemsGroupByTemplate.keySet()) {
            final List<BottariTemplateItem> templateItems = itemsGroupByTemplate.getOrDefault(
                    bottariTemplate,
                    List.of()
            );
            responses.add(ReadBottariTemplateResponse.of(bottariTemplate, templateItems));
        }

        return responses;
    }

    private void validateDuplicateItemNames(final List<String> itemNames) {
        final Set<String> uniqueItemNames = new HashSet<>();
        for (final String itemName : itemNames) {
            if (!uniqueItemNames.add(itemName)) {
                throw new BusinessException(ErrorCode.BOTTARI_TEMPLATE_ITEM_DUPLICATE_IN_REQUEST);
            }
        }
    }

    private void validateOwner(
            final String ssaid,
            final BottariTemplate bottariTemplate
    ) {
        if (!bottariTemplate.isOwner(ssaid)) {
            throw new BusinessException(ErrorCode.BOTTARI_TEMPLATE_NOT_OWNED, "본인의 보따리 템플릿이 아닙니다.");
        }
    }

    private void increaseTakenCount(
            final BottariTemplate bottariTemplate,
            final Member member
    ) {
        if (alreadyTookBottariTemplate(bottariTemplate, member)) {
            return;
        }
        try {
            final BottariTemplateHistory bottariTemplateHistory = new BottariTemplateHistory(member.getId(), bottariTemplate.getId());
            bottariTemplateHistoryRepository.save(bottariTemplateHistory);
            bottariTemplateRepository.plusTakenCountById(bottariTemplate.getId());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("단기간 내 보따리 템플릿 가져갈 수 없습니다.");
        }
    }

    private boolean alreadyTookBottariTemplate(
            final BottariTemplate bottariTemplate,
            final Member member
    ) {
        return bottariTemplateHistoryRepository.existsByBottariTemplateIdAndMemberId(bottariTemplate.getId(), member.getId());
    }
}
