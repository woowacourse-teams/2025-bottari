package com.bottari.service;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.repository.BottariItemRepository;
import com.bottari.repository.BottariRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariItemService {

    private final BottariItemRepository bottariItemRepository;
    private final BottariRepository bottariRepository;

    @Transactional
    public Long create(
            final Long bottariId,
            final CreateBottariItemRequest request
    ) {
        final Bottari bottari = bottariRepository.findById(bottariId)
                .orElseThrow(() -> new IllegalArgumentException("보따리를 찾을 수 없습니다."));
        validateDuplicateName(bottariId, request.name());
        final BottariItem bottariItem = new BottariItem(request.name(), bottari);
        final BottariItem savedBottariItem = bottariItemRepository.save(bottariItem);

        return savedBottariItem.getId();
    }

    public void delete(
            final Long bottariId,
            final Long id
    ) {
        validateItemInBottari(bottariId, id);
        bottariItemRepository.deleteById(id);
    }

    @Transactional
    public void check(
            final Long bottariId,
            final Long id
    ) {
        final BottariItem bottariItem = bottariItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보따리 물품을 찾을 수 없습니다."));
        validateItemInBottari(bottariId, id);
        bottariItem.check();
    }

    @Transactional
    public void uncheck(
            final Long bottariId,
            final Long id
    ) {
        final BottariItem bottariItem = bottariItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보따리 물품을 찾을 수 없습니다."));
        validateItemInBottari(bottariId, id);
        bottariItem.uncheck();
    }

    private void validateDuplicateName(
            final Long bottariId,
            final String name
    ) {
        if (bottariItemRepository.existsByBottariIdAndName(bottariId, name)) {
            throw new IllegalArgumentException("중복된 보따리 물품명입니다.");
        }
    }

    private void validateItemInBottari(
            final Long bottariId,
            final Long id
    ) {
        if (!bottariItemRepository.existsByBottariIdAndId(bottariId, id)) {
            throw new IllegalArgumentException("해당 보따리 내에 존재하는 물품이 아닙니다.");
        }
    }
}
