package com.bottari.service;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;
import com.bottari.dto.ReadBottariTemplateResponse;
import com.bottari.repository.BottariTemplateItemRepository;
import com.bottari.repository.BottariTemplateRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BottariTemplateService {

    private final BottariTemplateRepository bottariTemplateRepository;
    private final BottariTemplateItemRepository bottariTemplateItemRepository;

    public List<ReadBottariTemplateResponse> getAll() {
        final List<BottariTemplate> bottariTemplates = bottariTemplateRepository.findAllWithMember();
        final List<ReadBottariTemplateResponse> responses = new ArrayList<>();
        for (BottariTemplate bottariTemplate : bottariTemplates) {
            final List<BottariTemplateItem> bottariTemplateItems =
                    bottariTemplateItemRepository.findAllByBottariTemplateId(bottariTemplate.getId());
            responses.add(ReadBottariTemplateResponse.of(bottariTemplate, bottariTemplateItems));
        }

        return responses;
    }

    public ReadBottariTemplateResponse getById(final Long id) {
        final BottariTemplate bottariTemplate = bottariTemplateRepository.findByIdWithMember(id)
                .orElseThrow(() -> new IllegalArgumentException("보따리 템플릿을 찾을 수 없습니다."));
        final List<BottariTemplateItem> bottariTemplateItems =
                bottariTemplateItemRepository.findAllByBottariTemplateId(bottariTemplate.getId());

        return ReadBottariTemplateResponse.of(bottariTemplate, bottariTemplateItems);
    }
}
