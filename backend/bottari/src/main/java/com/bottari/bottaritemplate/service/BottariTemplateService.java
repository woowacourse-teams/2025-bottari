package com.bottari.bottaritemplate.service;

import com.bottari.bottari.domain.Bottari;
import com.bottari.bottari.domain.BottariItem;
import com.bottari.bottari.repository.BottariItemRepository;
import com.bottari.bottari.repository.BottariRepository;
import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateCursor;
import com.bottari.bottaritemplate.domain.BottariTemplateFetcher;
import com.bottari.bottaritemplate.domain.BottariTemplateHashtag;
import com.bottari.bottaritemplate.domain.BottariTemplateHashtagCursor;
import com.bottari.bottaritemplate.domain.BottariTemplateHistory;
import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import com.bottari.bottaritemplate.domain.BottariTemplateTitleCursor;
import com.bottari.bottaritemplate.domain.Hashtag;
import com.bottari.bottaritemplate.domain.SortProperty;
import com.bottari.bottaritemplate.dto.CreateBottariTemplateRequest;
import com.bottari.bottaritemplate.dto.ReadBottariTemplateResponse;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateByHashtagRequest;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateByTitleRequest;
import com.bottari.bottaritemplate.dto.ReadNextBottariTemplateResponse;
import com.bottari.bottaritemplate.repository.BottariTemplateHashtagRepository;
import com.bottari.bottaritemplate.repository.BottariTemplateHistoryRepository;
import com.bottari.bottaritemplate.repository.BottariTemplateItemRepository;
import com.bottari.bottaritemplate.repository.BottariTemplateRepository;
import com.bottari.bottaritemplate.repository.HashtagRepository;
import com.bottari.bottaritemplate.repository.dto.BottariTemplateProjection;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.member.domain.Member;
import com.bottari.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariTemplateService {

    private final BottariTemplateRepository bottariTemplateRepository;
    private final BottariTemplateItemRepository bottariTemplateItemRepository;
    private final BottariTemplateHashtagRepository bottariTemplateHashtagRepository;
    private final BottariTemplateHistoryRepository bottariTemplateHistoryRepository;
    private final HashtagRepository hashtagRepository;
    private final BottariRepository bottariRepository;
    private final BottariItemRepository bottariItemRepository;
    private final MemberRepository memberRepository;

    public ReadBottariTemplateResponse getById(final Long id) {
        final BottariTemplate bottariTemplate = bottariTemplateRepository.findByIdWithMember(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_TEMPLATE_NOT_FOUND));
        final List<BottariTemplateItem> bottariTemplateItems =
                bottariTemplateItemRepository.findAllByBottariTemplateId(bottariTemplate.getId());
        final List<Hashtag> hashtags = bottariTemplateHashtagRepository.findAllByBottariTemplateId(
                        bottariTemplate.getId()).stream()
                .map(BottariTemplateHashtag::getHashtag)
                .toList();

        return ReadBottariTemplateResponse.of(bottariTemplate, bottariTemplateItems, hashtags);
    }

    public List<ReadBottariTemplateResponse> getBySsaid(final String ssaid) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final List<BottariTemplate> bottariTemplates =
                bottariTemplateRepository.findAllByMemberIdWithMember(member.getId());
        final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate =
                groupingItemsByTemplate(bottariTemplates);
        final Map<BottariTemplate, List<Hashtag>> hashtagsGroupByTemplate =
                groupingHashtagsByTemplate(bottariTemplates);

        return buildReadBottariTemplateResponses(bottariTemplates, itemsGroupByTemplate, hashtagsGroupByTemplate);
    }

    public List<ReadBottariTemplateResponse> getAll(final String query) {
        final List<BottariTemplate> bottariTemplates = bottariTemplateRepository.findAllWithMember(query);
        final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate =
                groupingItemsByTemplate(bottariTemplates);
        final Map<BottariTemplate, List<Hashtag>> hashtagsGroupByTemplate =
                groupingHashtagsByTemplate(bottariTemplates);

        return buildReadBottariTemplateResponses(bottariTemplates, itemsGroupByTemplate, hashtagsGroupByTemplate);
    }

    public ReadNextBottariTemplateResponse getNextAllByTitle(final ReadNextBottariTemplateByTitleRequest request) {
        final BottariTemplateTitleCursor cursor = request.toCursor();
        return getNextAll(cursor, c -> getNextBySortProperty(c, c.toPageable()));
    }

    public ReadNextBottariTemplateResponse getNextAllByHashTag(final ReadNextBottariTemplateByHashtagRequest request) {
        final BottariTemplateHashtagCursor cursor = request.toCursor();
        return getNextAll(cursor, c -> getNextBySortProperty(c, c.toPageable()));
    }

    @Transactional
    public Long create(
            final String ssaid,
            final CreateBottariTemplateRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND, "등록되지 않은 ssaid입니다."));
        final BottariTemplate bottariTemplate = new BottariTemplate(request.title(), request.description(), member);
        final BottariTemplate savedBottariTemplate = bottariTemplateRepository.save(bottariTemplate);
        validateDuplicateItemNames(request.bottariTemplateItems());
        final List<BottariTemplateItem> bottariTemplateItems = request.bottariTemplateItems()
                .stream()
                .map(name -> new BottariTemplateItem(name, savedBottariTemplate))
                .toList();
        bottariTemplateItemRepository.saveAll(bottariTemplateItems);
        saveHashtagsIfPresent(request, savedBottariTemplate);

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

    private <T extends BottariTemplateCursor> ReadNextBottariTemplateResponse getNextAll(
            final T cursor,
            final BottariTemplateFetcher<T> bottariTemplateFetcher
    ) {
        final Pageable pageable = cursor.toPageable();
        final Slice<BottariTemplateProjection> bottariTemplates = bottariTemplateFetcher.fetch(cursor);
        final Map<Long, List<BottariTemplateItem>> itemsGroupByTemplateId =
                groupingItemsByTemplateId(bottariTemplates.getContent());
        final Map<Long, List<Hashtag>> hashtagsGroupByTemplateId =
                groupingHashtagsByTemplateId(bottariTemplates.getContent());
        final List<ReadBottariTemplateResponse> responses = buildReadBottariTemplateResponses(
                itemsGroupByTemplateId,
                hashtagsGroupByTemplateId,
                bottariTemplates.getContent()
        );

        return ReadNextBottariTemplateResponse.of(
                new SliceImpl<>(responses, pageable, bottariTemplates.hasNext()), cursor.getProperty());
    }

    private Slice<BottariTemplateProjection> getNextBySortProperty(
            final BottariTemplateTitleCursor cursor,
            final Pageable pageable
    ) {
        final SortProperty property = SortProperty.fromProperty(cursor.getProperty());
        final int limit = pageable.getPageSize() + 1;
        return switch (property) {
            case SortProperty.CREATED_AT -> toSlice(pageable, bottariTemplateRepository.findNextByCreatedAt(
                    cursor.getTitle(), cursor.getCreatedAt(), cursor.getLastId(), limit));
            case SortProperty.TAKEN_COUNT -> toSlice(pageable, bottariTemplateRepository.findNextByTakenCount(
                    cursor.getTitle(), cursor.getTakenCount(), cursor.getLastId(), limit));
        };
    }

    private Slice<BottariTemplateProjection> getNextBySortProperty(
            final BottariTemplateHashtagCursor cursor,
            final Pageable pageable
    ) {
        final SortProperty property = SortProperty.fromProperty(cursor.getProperty());
        return switch (property) {
            case SortProperty.CREATED_AT -> bottariTemplateHashtagRepository.findNextByCreatedAt(
                    cursor.getHashtagId(), cursor.getCreatedAt(), cursor.getLastId(), pageable);
            case SortProperty.TAKEN_COUNT -> bottariTemplateHashtagRepository.findNextByTakenCount(
                    cursor.getHashtagId(), cursor.getTakenCount(), cursor.getLastId(), pageable);
        };
    }

    private Slice<BottariTemplateProjection> toSlice(
            final Pageable pageable,
            final List<BottariTemplateProjection> bottariTemplateProjections
    ) {
        final boolean hasNext = bottariTemplateProjections.size() > pageable.getPageSize();
        List<BottariTemplateProjection> projections = new ArrayList<>(bottariTemplateProjections);
        if (hasNext) {
            projections = projections.subList(0, pageable.getPageSize());
        }
        return new SliceImpl<>(projections, pageable, hasNext);
    }

    private Map<BottariTemplate, List<BottariTemplateItem>> groupingItemsByTemplate(
            final List<BottariTemplate> bottariTemplates
    ) {
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

    private Map<BottariTemplate, List<Hashtag>> groupingHashtagsByTemplate(
            final List<BottariTemplate> bottariTemplateItems) {
        final Map<BottariTemplate, List<Hashtag>> groupByTemplates = new LinkedHashMap<>();
        final List<BottariTemplateHashtag> bottariTemplateHashtags =
                bottariTemplateHashtagRepository.findAllByBottariTemplateIn(bottariTemplateItems);
        for (final BottariTemplate bottariTemplate : bottariTemplateItems) {
            groupByTemplates.put(bottariTemplate, new ArrayList<>());
        }
        for (final BottariTemplateHashtag item : bottariTemplateHashtags) {
            final BottariTemplate bottariTemplate = item.getBottariTemplate();
            groupByTemplates.get(bottariTemplate).add(item.getHashtag());
        }

        return groupByTemplates;
    }

    private Map<Long, List<BottariTemplateItem>> groupingItemsByTemplateId(
            final List<BottariTemplateProjection> bottariTemplates
    ) {
        final Map<Long, List<BottariTemplateItem>> groupByTemplates = new LinkedHashMap<>();
        final List<Long> ids = bottariTemplates.stream()
                .map(BottariTemplateProjection::getBottariTemplateId)
                .toList();
        final List<BottariTemplateItem> items = bottariTemplateItemRepository.findAllByBottariTemplateIds(ids);
        for (final Long id : ids) {
            groupByTemplates.put(id, new ArrayList<>());
        }
        for (final BottariTemplateItem item : items) {
            final BottariTemplate bottariTemplate = item.getBottariTemplate();
            groupByTemplates.get(bottariTemplate.getId()).add(item);
        }

        return groupByTemplates;
    }

    private Map<Long, List<Hashtag>> groupingHashtagsByTemplateId(
            final List<BottariTemplateProjection> bottariTemplates
    ) {
        final Map<Long, List<Hashtag>> groupByTemplates = new LinkedHashMap<>();
        final List<Long> ids = bottariTemplates.stream()
                .map(BottariTemplateProjection::getBottariTemplateId)
                .toList();
        final List<BottariTemplateHashtag> hashtags = bottariTemplateHashtagRepository.findAllByBottariTemplateIds(ids);
        for (final Long id : ids) {
            groupByTemplates.put(id, new ArrayList<>());
        }
        for (final BottariTemplateHashtag hashtag : hashtags) {
            final BottariTemplate bottariTemplate = hashtag.getBottariTemplate();
            groupByTemplates.get(bottariTemplate.getId()).add(hashtag.getHashtag());
        }

        return groupByTemplates;
    }

    private List<ReadBottariTemplateResponse> buildReadBottariTemplateResponses(
            final List<BottariTemplate> bottariTemplates,
            final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate,
            final Map<BottariTemplate, List<Hashtag>> hashtagsGroupByTemplate
    ) {
        final List<ReadBottariTemplateResponse> responses = new ArrayList<>();
        for (final BottariTemplate bottariTemplate : bottariTemplates) {
            final List<BottariTemplateItem> templateItems = itemsGroupByTemplate.getOrDefault(
                    bottariTemplate,
                    List.of()
            );
            final List<Hashtag> hashtags = hashtagsGroupByTemplate.getOrDefault(
                    bottariTemplate,
                    List.of()
            );
            responses.add(ReadBottariTemplateResponse.of(bottariTemplate, templateItems, hashtags));
        }

        return responses;
    }

    private List<ReadBottariTemplateResponse> buildReadBottariTemplateResponses(
            final Map<Long, List<BottariTemplateItem>> itemsGroupByTemplate,
            final Map<Long, List<Hashtag>> hashtagsGroupByTemplate,
            final List<BottariTemplateProjection> projections
    ) {
        final List<ReadBottariTemplateResponse> responses = new ArrayList<>();
        for (final BottariTemplateProjection projection : projections) {
            final List<BottariTemplateItem> items = itemsGroupByTemplate.getOrDefault(
                    projection.getBottariTemplateId(),
                    List.of()
            );
            final List<Hashtag> hashtags = hashtagsGroupByTemplate.getOrDefault(
                    projection.getBottariTemplateId(),
                    List.of()
            );
            responses.add(ReadBottariTemplateResponse.of(projection, items, hashtags));
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

    private void saveHashtagsIfPresent(
            final CreateBottariTemplateRequest request,
            final BottariTemplate savedBottariTemplate
    ) {
        if (request.hashtagNames() != null && !request.hashtagNames().isEmpty()) {
            saveHashtags(request.hashtagNames(), savedBottariTemplate);
        }
    }

    private void saveHashtags(
            final List<String> hashtagNames,
            final BottariTemplate bottariTemplate
    ) {
        validateDuplicateHashtagNames(hashtagNames);
        validateHashtagCount(hashtagNames);
        final List<Hashtag> allHashtags = findOrCreateHashtags(hashtagNames);
        final List<BottariTemplateHashtag> templateHashtags = allHashtags.stream()
                .map(hashtag -> new BottariTemplateHashtag(bottariTemplate, hashtag))
                .toList();
        bottariTemplateHashtagRepository.saveAll(templateHashtags);
    }

    private void validateDuplicateHashtagNames(final List<String> hashtagNames) {
        final Set<String> uniqueHashtagNames = new HashSet<>(hashtagNames);
        if (uniqueHashtagNames.size() != hashtagNames.size()) {
            throw new BusinessException(ErrorCode.HASHTAG_DUPLICATE_IN_REQUEST);
        }
    }

    private void validateHashtagCount(final List<String> hashtagNames) {
        final int maxHashtagCount = 10;
        if (hashtagNames.size() > maxHashtagCount) {
            throw new BusinessException(ErrorCode.HASHTAG_TOO_MANY, "최대 %d개까지 입력 가능합니다.".formatted(maxHashtagCount));
        }
    }

    private List<Hashtag> findOrCreateHashtags(final List<String> hashtagNames) {
        final List<Hashtag> existingHashtags = hashtagRepository.findAllByNameIn(hashtagNames);
        final Set<String> existingHashtagNames = existingHashtags.stream()
                .map(Hashtag::getName)
                .collect(Collectors.toSet());
        final List<Hashtag> newHashtags = hashtagNames.stream()
                .filter(name -> !existingHashtagNames.contains(name))
                .map(Hashtag::new)
                .toList();
        if (!newHashtags.isEmpty()) {
            hashtagRepository.saveAll(newHashtags);
        }
        final List<Hashtag> allHashtagsToAssociate = new ArrayList<>(existingHashtags);
        allHashtagsToAssociate.addAll(newHashtags);

        return allHashtagsToAssociate;
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
            final BottariTemplateHistory bottariTemplateHistory =
                    new BottariTemplateHistory(member.getId(), bottariTemplate.getId());
            bottariTemplateHistoryRepository.save(bottariTemplateHistory);
            bottariTemplateRepository.plusTakenCountById(bottariTemplate.getId());
        } catch (final DataIntegrityViolationException exception) {
            throw new BusinessException(ErrorCode.BOTTARI_TEMPLATE_ALREADY_TAKEN_RECENTLY);
        }
    }

    private boolean alreadyTookBottariTemplate(
            final BottariTemplate bottariTemplate,
            final Member member
    ) {
        return bottariTemplateHistoryRepository.existsByBottariTemplateIdAndMemberId(
                bottariTemplate.getId(),
                member.getId()
        );
    }
}
