package com.bottari.service;

import com.bottari.domain.Bottari;
import com.bottari.domain.BottariItem;
import com.bottari.dto.CreateBottariItemRequest;
import com.bottari.dto.EditBottariItemsRequest;
import com.bottari.dto.ReadBottariItemResponse;
import com.bottari.repository.BottariItemRepository;
import com.bottari.repository.BottariRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Transactional
    public void update(
            final Long bottariId,
            final EditBottariItemsRequest request
    ) {
        final Bottari bottari = bottariRepository.findById(bottariId)
                .orElseThrow(() -> new IllegalArgumentException("보따리를 찾을 수 없습니다."));
        validateDuplicateDeleteItemIds(request.deleteItemIds());
        validateAllItemsInBottari(bottariId, request.deleteItemIds());
        bottariItemRepository.deleteByIdIn(request.deleteItemIds());
        validateUpdateItemNames(bottariId, request.createItemNames());
        final List<BottariItem> bottariItems = request.createItemNames()
                .stream()
                .map(name -> new BottariItem(name, bottari))
                .toList();
        bottariItemRepository.saveAll(bottariItems);
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

    private void validateDuplicateDeleteItemIds(final List<Long> deleteIds) {
        final Set<Long> uniqueDeleteIds = new HashSet<>();
        for (final Long deleteId : deleteIds) {
            if (!uniqueDeleteIds.add(deleteId)) {
                throw new IllegalArgumentException("삭제하려는 아이템에 중복이 있습니다.");
            }
        }
    }

    private void validateAllItemsInBottari(
            final Long bottariId,
            final List<Long> itemIds
    ) {
        final int countItemInBottari = bottariItemRepository.countAllByBottariIdAndIdIn(bottariId, itemIds);
        if (countItemInBottari != itemIds.size()) {
            throw new IllegalArgumentException("보따리 안에 없는 물품은 삭제할 수 없습니다.");
        }
    }

    private void validateUpdateItemNames(
            final Long bottariId,
            final List<String> itemNames
    ) {
        validateDuplicateItemNames(itemNames);
        validateExistsItemNames(bottariId, itemNames);
    }

    private void validateExistsItemNames(
            final Long bottariId,
            final List<String> itemNames
    ) {
        if (bottariItemRepository.existsByBottariIdAndNameIn(bottariId, itemNames)) {
            throw new IllegalArgumentException("중복된 물품이 존재합니다.");
        }
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
