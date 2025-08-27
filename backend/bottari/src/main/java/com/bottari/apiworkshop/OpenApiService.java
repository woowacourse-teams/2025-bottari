package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import java.time.LocalDate;
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
            final String query,
            final LocalDate start,
            final LocalDate end,
            final int limit,
            final String lastName,
            final int lastRank,
            final Long lastCount
    ) {
        final List<Long> templateIds = collectTemplateIdsByQueryAndDate(query, start, end);
        final Slice<ItemProjection> itemProjections = getItemsWithIncludedCountByTemplatesAndCursor(
                templateIds, lastName, lastCount, limit);
        List<ItemResponse> itemResponses = createItemResponsesWithDenseRank(itemProjections.getContent(), lastRank, lastCount);

        return new MostIncludedResponse(
                query,
                start,
                end,
                createCursor(limit, itemProjections, itemResponses),
                itemResponses
        );
    }

    private List<Long> collectTemplateIdsByQueryAndDate(
            final String query,
            final LocalDate start,
            final LocalDate end
    ) {
        final LocalDateTime startDateTime = start == null ? null : start.atStartOfDay();
        final LocalDateTime endDateTime = end == null ? null : end.atTime(23, 59, 59);
        final List<BottariTemplate> templates = templateRepository.findAllByTitleContainingAndCreatedAtBetween(
                query,
                startDateTime,
                endDateTime
        );

        return templates.stream()
                .map(BottariTemplate::getId)
                .toList();
    }

    private Slice<ItemProjection> getItemsWithIncludedCountByTemplatesAndCursor(
            final List<Long> templateIds,
            final String lastName,
            final Long lastCount,
            final int limit
    ) {
        final PageRequest pageable = PageRequest.ofSize(limit);

        return itemRepository.findAllWithIncludedCountByTemplateIdAndCursor(
                templateIds,
                lastName,
                lastCount,
                pageable
        );
    }

    private List<ItemResponse> createItemResponsesWithDenseRank(
            final List<ItemProjection> itemProjections,
            final int lastRank,
            final Long lastCount
    ) {
        final List<ItemResponse> itemResponses = new ArrayList<>();
        int currentRank = lastRank;
        Long prevCount = lastCount;
        for (ItemProjection projection : itemProjections) {
            Long count = projection.getIncludedCount();
            currentRank = calculateCurrentRank(prevCount, count, currentRank);
            itemResponses.add(ItemResponse.of(currentRank, projection));
            prevCount = count;
        }

        return itemResponses;
    }

    private int calculateCurrentRank(
            final Long prevCount,
            final Long count,
            int currentRank
    ) {
        if (Objects.equals(count, prevCount)) {
            return currentRank;
        }
        return currentRank + 1;
    }

    private Cursor createCursor(
            final int limit,
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
