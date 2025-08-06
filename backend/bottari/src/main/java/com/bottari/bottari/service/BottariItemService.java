package com.bottari.bottari.service;

import com.bottari.bottari.domain.Bottari;
import com.bottari.bottari.domain.BottariItem;
import com.bottari.bottari.repository.BottariItemRepository;
import com.bottari.bottari.repository.BottariRepository;
import com.bottari.bottari.dto.CreateBottariItemRequest;
import com.bottari.bottari.dto.EditBottariItemsRequest;
import com.bottari.bottari.dto.ReadBottariItemResponse;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BottariItemService {

    private static final int MAX_BOTTARI_ITEMS_COUNT = 200;

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
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_NOT_FOUND));
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
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_NOT_FOUND));
        validateDuplicateDeleteItemIds(request.deleteItemIds());
        validateAllItemsInBottari(bottariId, request.deleteItemIds());
        bottariItemRepository.deleteByIdIn(request.deleteItemIds());
        validateTotalItemCount(bottariId, request.createItemNames());
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
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_ITEM_NOT_FOUND));
        bottariItem.check();
    }

    @Transactional
    public void uncheck(final Long id) {
        final BottariItem bottariItem = bottariItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_ITEM_NOT_FOUND));
        bottariItem.uncheck();
    }

    private void validateExistsBottari(final Long bottariId) {
        if (!bottariRepository.existsById(bottariId)) {
            throw new BusinessException(ErrorCode.BOTTARI_NOT_FOUND);
        }
    }

    private void validateDuplicateName(
            final Long bottariId,
            final String name
    ) {
        if (bottariItemRepository.existsByBottariIdAndName(bottariId, name)) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_ALREADY_EXISTS);
        }
    }

    private void validateDuplicateDeleteItemIds(final List<Long> deleteIds) {
        final Set<Long> uniqueDeleteIds = new HashSet<>();
        for (final Long deleteId : deleteIds) {
            if (!uniqueDeleteIds.add(deleteId)) {
                throw new BusinessException(ErrorCode.BOTTARI_ITEM_DUPLICATE_IN_REQUEST, "삭제하려는 물품 id가 중복되었습니다.");
            }
        }
    }

    private void validateAllItemsInBottari(
            final Long bottariId,
            final List<Long> itemIds
    ) {
        final int countItemInBottari = bottariItemRepository.countAllByBottariIdAndIdIn(bottariId, itemIds);
        if (countItemInBottari != itemIds.size()) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_NOT_IN_BOTTARI, "삭제할 수 없습니다.");
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
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_ALREADY_EXISTS);
        }
    }

    private void validateDuplicateItemNames(final List<String> itemNames) {
        final Set<String> uniqueItemNames = new HashSet<>();
        for (final String itemName : itemNames) {
            if (!uniqueItemNames.add(itemName)) {
                throw new BusinessException(ErrorCode.BOTTARI_ITEM_DUPLICATE_IN_REQUEST);
            }
        }
    }

    private void validateTotalItemCount(
            final Long bottariId,
            final List<String> itemNames
    ) {
        final int bottariItemCount = bottariItemRepository.countAllByBottariId(bottariId);
        final int totalItemCount = bottariItemCount + itemNames.size();
        if (totalItemCount > MAX_BOTTARI_ITEMS_COUNT) {
            throw new BusinessException(ErrorCode.BOTTARI_ITEM_MAXIMUM_EXCEEDED, MAX_BOTTARI_ITEMS_COUNT + "개 초과");
        }
    }
}
