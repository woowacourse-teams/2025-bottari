package com.bottari.apiworkshop;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
            final Long lastId,
            final int lastRank,
            final int lastCount
    ) {
        // 1. 기간 내에 생성된 템플릿 조회
        // TODO: query가 null일 때 처리 & findByTitleContaining 성능 문제
        final List<BottariTemplate> templates = templateRepository.findByTitleContaining(query);
        final List<BottariTemplate> inDurationTemplates = templates.stream()
                .filter(template -> template.getCreatedAt().isAfter(start.atStartOfDay()) &&
                                    template.getCreatedAt().isBefore(end.atTime(23, 59, 59)))
                .toList();
        final List<Long> inDurationTemplateIds = inDurationTemplates.stream()
                .map(BottariTemplate::getId)
                .toList();

        // 2. 기간 내에 생성된 템플릿에 포함된 아이템 조회
        final List<BottariTemplateItem> items = itemRepository.findAllByBottariTemplateIdIn(inDurationTemplateIds);

        // 3. 아이템별 포함된 횟수 집계 및 정렬
        final Map<String, Long> collectByCount = items.stream()
                .collect(Collectors.groupingBy(BottariTemplateItem::getName, Collectors.counting()));
        final List<Entry<String, Long>> sortedByCount = collectByCount.entrySet().stream()
                .sorted((e1, e2) -> {
                    int countComparison = e2.getValue().compareTo(e1.getValue());
                    if (countComparison != 0) {
                        return countComparison;
                    }
                    return e1.getKey().compareTo(e2.getKey()); // 이름순 정렬
                })
                .toList();

        // 4. 아이템 응답 생성
        List<com.bottari.apiworkshop.ItemResponse> itemResponses = new ArrayList<>();
        int currentRank = 1;
        Long prevCount = null;
        for (Entry<String, Long> entry : sortedByCount) {
            Long count = entry.getValue();
            if (prevCount != null && !Objects.equals(count, prevCount)) {
                currentRank = itemResponses.size() + 1;
            }
            itemResponses.add(new ItemResponse(currentRank, entry.getKey(), entry.getValue()));
            prevCount = count;
        }

        // TODO: 페이징 처리
        return new MostIncludedResponse(
                query,
                start != null ? start.toString() : null,
                end != null ? end.toString() : null,
                null,
                itemResponses.stream().limit(limit).toList()
        );
    }
}
