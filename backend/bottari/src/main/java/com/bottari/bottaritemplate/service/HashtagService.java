package com.bottari.bottaritemplate.service;

import com.bottari.bottaritemplate.dto.ReadPopularHashtagResponse;
import com.bottari.bottaritemplate.repository.BottariTemplateHashtagRepository;
import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final BottariTemplateHashtagRepository bottariTemplateHashtagRepository;

    @Transactional(readOnly = true)
    public List<ReadPopularHashtagResponse> getTopHashtagsByUsageCount(final int limit) {
        final List<HashtagPopularityProjection> projections =
                bottariTemplateHashtagRepository.findTopNByUsageCount(limit);

        return buildReadPopularHashtagResponses(projections);
    }

    private static List<ReadPopularHashtagResponse> buildReadPopularHashtagResponses(final List<HashtagPopularityProjection> projections) {
        final List<ReadPopularHashtagResponse> responses = new ArrayList<>();
        for (int i = 0; i < projections.size(); i++) {
            final HashtagPopularityProjection projection = projections.get(i);
            responses.add(ReadPopularHashtagResponse.of(i + 1, projection));
        }

        return responses;
    }
}
