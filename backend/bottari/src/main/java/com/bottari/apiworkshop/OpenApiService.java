package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import java.time.LocalDate;
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
        // 1. 기간 내에 생성된 템플릿 조회
        final List<BottariTemplate> templates = templateRepository.findAllByTitleContainingAndCreatedAtBetween(
                query,
                start == null ? null : start.atStartOfDay(),
                end == null ? null : end.atTime(23, 59, 59)
        );
        final List<Long> templateIds = templates.stream()
                .map(BottariTemplate::getId)
                .toList();

        // 2. 기간 내에 생성된 템플릿에 포함된 아이템 조회 / 아이템별 포함된 횟수 집계 및 정렬
        final Slice<ItemProjection> itemProjections = itemRepository.findAllWithIncludedCountByTemplateIdInCursor(
                templateIds,
                lastName,
                lastCount,
                PageRequest.ofSize(limit)
        );

        // 3. 아이템 응답 생성
        List<ItemResponse> itemResponses = new ArrayList<>();
        int currentRank = lastRank + 1;
        Long prevCount = null;
        for (ItemProjection projection : itemProjections) {
            Long count = projection.getIncludedCount();
            if (prevCount != null && !Objects.equals(count, prevCount)) {
                currentRank++;
            }
            if (Objects.equals(count, lastCount)) {
                currentRank--;
            }
            itemResponses.add(ItemResponse.of(currentRank, projection));
            prevCount = count;
        }

        final Cursor cursor = new Cursor(
                limit,
                itemProjections.getContent().getLast().getName(),
                itemProjections.hasNext(),
                itemResponses.getLast().rank(),
                itemResponses.getLast().includedCount()
        );

        return new MostIncludedResponse(
                query,
                start,
                end,
                cursor,
                itemResponses
        );
    }
}
