package com.bottari.service;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;
import com.bottari.domain.Member;
import com.bottari.dto.CreateBottariTemplateRequest;
import com.bottari.repository.BottariItemRepository;
import com.bottari.repository.BottariRepository;
import com.bottari.repository.BottariTemplateItemRepository;
import com.bottari.repository.BottariTemplateRepository;
import com.bottari.repository.MemberRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariTemplateService {

    private final BottariTemplateRepository bottariTemplateRepository;
    private final BottariTemplateItemRepository bottariTemplateItemRepository;
    private final BottariRepository bottariRepository;
    private final BottariItemRepository bottariItemRepository;
    private final MemberRepository memberRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("해당 템플릿을 찾을 수 없습니다."));
        final List<BottariTemplateItem> bottariTemplateItems = bottariTemplateItemRepository.findAllByBottariTemplateId(id);
        final Member member = memberRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new IllegalArgumentException("해당 ssaid로 가입된 사용자가 없습니다."));
        final Bottari bottari = new Bottari(bottariTemplate.getTitle(),member);
        final Bottari savedBottari = bottariRepository.save(bottari);
        final List<BottariItem> bottariItems = bottariTemplateItems.stream()
                .map(item -> new BottariItem(item.getName(),bottari))
                .toList();
        bottariItemRepository.saveAll(bottariItems);

        return savedBottari.getId();
    }

    private void validateDuplicateItemNames(final List<String> itemNames) {
        final Set<String> uniqueItemNames = new HashSet<>();
        for (final String itemName : itemNames) {
            if (!uniqueItemNames.add(itemName)) {
                throw new IllegalArgumentException("중복된 물품이 존재합니다.");
            }
        }
    }
}
