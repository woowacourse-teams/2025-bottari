package com.bottari.service;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateHistory;
import com.bottari.domain.BottariTemplateItem;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.repository.BottariItemRepository;
import com.bottari.repository.BottariRepository;
import com.bottari.repository.BottariTemplateHistoryRepository;
import com.bottari.repository.BottariTemplateItemRepository;
import com.bottari.repository.BottariTemplateRepository;
import com.bottari.repository.MemberRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new IllegalArgumentException("보따리 템플릿을 찾을 수 없습니다."));
        final List<BottariTemplateItem> bottariTemplateItems =
                bottariTemplateItemRepository.findAllByBottariTemplateId(bottariTemplate.getId());

        return ReadBottariTemplateResponse.of(bottariTemplate, bottariTemplateItems);
    }

    public List<ReadBottariTemplateResponse> getBySsaid(final String ssaid) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new IllegalArgumentException("해당 ssaid로 가입된 사용자가 없습니다."));
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

    @Transactional
    public Long create(
            final String ssaid,
            final CreateBottariTemplateRequest request
    ) {
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new IllegalArgumentException("해당 ssaid로 가입된 사용자가 없습니다."));
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
                .orElseThrow(() -> new IllegalArgumentException("해당 보따리 템플릿을 찾을 수 없습니다."));
        final List<BottariTemplateItem> bottariTemplateItems = bottariTemplateItemRepository.findAllByBottariTemplateId(id);
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new IllegalArgumentException("해당 ssaid로 가입된 사용자가 없습니다."));
        final Bottari bottari = new Bottari(bottariTemplate.getTitle(), member);
        final Bottari savedBottari = bottariRepository.save(bottari);
        final List<BottariItem> bottariItems = bottariTemplateItems.stream()
                .map(item -> new BottariItem(item.getName(), bottari))
                .toList();
        bottariItemRepository.saveAll(bottariItems);
        increaseTakenCount(bottariTemplate, member);
        return savedBottari.getId();
    }

    private void increaseTakenCount(
            final BottariTemplate bottariTemplate,
            final Member member
    ) {
        if(!bottariTemplateHistoryRepository.existsByBottariTemplateIdAndMemberId(bottariTemplate.getId(), member.getId())){
            bottariTemplate.increaseTakenCount();
            BottariTemplateHistory bottariTemplateHistory = new BottariTemplateHistory(member, bottariTemplate);
            bottariTemplateHistoryRepository.save(bottariTemplateHistory);
        }
    }

    @Transactional
    public void deleteById(
            final Long id,
            final String ssaid
    ) {
        final BottariTemplate bottariTemplate = bottariTemplateRepository.findByIdWithMember(id)
                .orElseThrow(() -> new IllegalArgumentException("보따리 템플릿을 찾을 수 없습니다."));
        validateOwner(ssaid, bottariTemplate);
        bottariTemplateItemRepository.deleteByBottariTemplateId(id);
        bottariTemplateRepository.deleteById(id);
    }

    private Map<BottariTemplate, List<BottariTemplateItem>> groupingItemsByTemplate(final List<BottariTemplate> bottariTemplates) {
        final List<BottariTemplateItem> items = bottariTemplateItemRepository.findAllByBottariTemplateIn(
                bottariTemplates);

        return items.stream()
                .collect(Collectors.groupingBy(BottariTemplateItem::getBottariTemplate));
    }

    private List<ReadBottariTemplateResponse> buildReadBottariTemplateResponses(final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate) {
        final List<ReadBottariTemplateResponse> responses = new ArrayList<>();
        final List<BottariTemplate> sortedBottariTemplates = sortByCreatedAtDesc(itemsGroupByTemplate);
        for (final BottariTemplate bottariTemplate : sortedBottariTemplates) {
            final List<BottariTemplateItem> templateItems = itemsGroupByTemplate.getOrDefault(
                    bottariTemplate,
                    List.of()
            );
            responses.add(ReadBottariTemplateResponse.of(bottariTemplate, templateItems));
        }

        return responses;
    }

    private List<BottariTemplate> sortByCreatedAtDesc(final Map<BottariTemplate, List<BottariTemplateItem>> itemsGroupByTemplate) {
        return itemsGroupByTemplate.keySet()
                .stream()
                .sorted(Comparator.comparing(BottariTemplate::getCreatedAt).reversed())
                .toList();
    }

    private void validateDuplicateItemNames(final List<String> itemNames) {
        final Set<String> uniqueItemNames = new HashSet<>();
        for (final String itemName : itemNames) {
            if (!uniqueItemNames.add(itemName)) {
                throw new IllegalArgumentException("중복된 물품이 존재합니다.");
            }
        }
    }

    private void validateOwner(
            final String ssaid,
            final BottariTemplate bottariTemplate
    ) {
        if (!bottariTemplate.isOwner(ssaid)) {
            throw new IllegalArgumentException("본인의 보따리 템플릿이 아닙니다.");
        }
    }
}
