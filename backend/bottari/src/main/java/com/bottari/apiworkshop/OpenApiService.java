package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenApiService {

    private final OpenApiTemplateRepository templateRepository;
    private final OpenApiItemRepository itemRepository;

    public MostIncludedResponse mostIncluded(
            final MostIncludedQuery query,
            final CursorRequest cursor
    ) {
        final List<Long> templateIds = collectTemplateIdsByQueryAndDate(query);
        if (templateIds.isEmpty()) {
            return MostIncludedResponse.empty(query);
        }
        final Slice<ItemProjection> itemProjections = getItemsWithIncludedCountByTemplatesAndCursor(
                templateIds, cursor);
        List<ItemResponse> itemResponses = createItemResponsesWithDenseRank(itemProjections.getContent(), cursor.lastRank(), cursor.lastCount());

        return MostIncludedResponse.of(
                query,
                createCursor(cursor.limit(), itemProjections, itemResponses),
                itemResponses
        );
    }

    private List<Long> collectTemplateIdsByQueryAndDate(
            final MostIncludedQuery query
    ) {
        final LocalDateTime startDateTime = query.start() == null ? null : query.start().atStartOfDay();
        final LocalDateTime endDateTime = query.end() == null ? null : query.end().atTime(23, 59, 59);
        final List<BottariTemplate> templates = templateRepository.findAllByTitleContainingAndCreatedAtBetween(
                query.query(),
                startDateTime,
                endDateTime
        );

        return templates.stream()
                .map(BottariTemplate::getId)
                .toList();
    }

    private Slice<ItemProjection> getItemsWithIncludedCountByTemplatesAndCursor(
            final List<Long> templateIds,
            final CursorRequest cursor
    ) {
        final PageRequest pageable = PageRequest.ofSize(Math.toIntExact(cursor.limit()));

        return itemRepository.findAllWithIncludedCountByTemplateIdAndCursor(
                templateIds,
                cursor.lastName(),
                cursor.lastCount(),
                pageable
        );
    }

    private List<ItemResponse> createItemResponsesWithDenseRank(
            final List<ItemProjection> itemProjections,
            final Long lastRank,
            final Long lastCount
    ) {
        final List<ItemResponse> itemResponses = new ArrayList<>();
        Long currentRank = lastRank;
        Long prevCount = lastCount;
        for (ItemProjection projection : itemProjections) {
            Long count = projection.getIncludedCount();
            currentRank = calculateCurrentRank(prevCount, count, currentRank);
            itemResponses.add(ItemResponse.of(currentRank, projection));
            prevCount = count;
        }

        return itemResponses;
    }

    private Long calculateCurrentRank(
            final Long prevCount,
            final Long count,
            final Long currentRank
    ) {
        if (Objects.equals(count, prevCount)) {
            return currentRank;
        }
        return currentRank + 1;
    }

    private Cursor createCursor(
            final Long limit,
            final Slice<ItemProjection> itemProjections,
            final List<ItemResponse> itemResponses
    ) {
        final ItemResponse lastItem = itemResponses.getLast();

        return new Cursor(
                limit,
                lastItem.name(),
                itemProjections.hasNext(),
                lastItem.rank(),
                lastItem.includedCount()
        );
    }
}
