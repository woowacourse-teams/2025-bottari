package com.bottari.service;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.dto.ReadBottariItemResponse;
import com.bottari.repository.BottariItemRepository;
import com.bottari.repository.BottariRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariItemService {

    private final BottariItemRepository bottariItemRepository;
    private final BottariRepository bottariRepository;

    public List<ReadBottariItemResponse> getAllByBottariId(final Long bottariId) {
        validateExistsBottari(bottariId);
        final List<BottariItem> bottariItems = bottariItemRepository.findAllByBottariId(bottariId);

        return bottariItems.stream()
                .map(ReadBottariItemResponse::from)
                .toList();
    }

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

    public void delete(final Long id) {
        bottariItemRepository.deleteById(id);
    }

    @Transactional
    public void check(final Long id) {
        final BottariItem bottariItem = bottariItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보따리 물품을 찾을 수 없습니다."));
        bottariItem.check();
    }

    @Transactional
    public void uncheck(final Long id) {
        final BottariItem bottariItem = bottariItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보따리 물품을 찾을 수 없습니다."));
        bottariItem.uncheck();
    }

    private void validateExistsBottari(final Long bottariId) {
        if (!bottariRepository.existsById(bottariId)) {
            throw new IllegalArgumentException("보따리를 찾을 수 없습니다.");
        }
    }

    private void validateDuplicateName(
            final Long bottariId,
            final String name
    ) {
        if (bottariItemRepository.existsByBottariIdAndName(bottariId, name)) {
            throw new IllegalArgumentException("중복된 보따리 물품명입니다.");
        }
    }
}
