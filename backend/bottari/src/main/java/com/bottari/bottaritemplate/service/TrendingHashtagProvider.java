package com.bottari.bottaritemplate.service;

import com.bottari.bottaritemplate.repository.BottariTemplateHashtagRepository;
import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrendingHashtagProvider {

    private final static int RECENT_DAYS = 7;

    private final BottariTemplateHashtagRepository bottariTemplateHashtagRepository;

    public List<HashtagPopularityProjection> getPopularHashtags(final int limit) {
        final LocalDateTime since = calculateSince();

        return bottariTemplateHashtagRepository.findTopNByUsageCountSince(since, limit);
    }

    private LocalDateTime calculateSince() {
        return LocalDateTime.now()
                .minusDays(RECENT_DAYS);
    }
}
