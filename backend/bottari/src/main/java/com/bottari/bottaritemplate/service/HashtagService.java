package com.bottari.bottaritemplate.service;

import com.bottari.bottaritemplate.dto.ReadHashtagWithUsageCountResponse;
import com.bottari.bottaritemplate.repository.BottariTemplateHashtagRepository;
import com.bottari.bottaritemplate.repository.dto.HashtagPopularityProjection;
import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final BottariTemplateHashtagRepository bottariTemplateHashtagRepository;

    @Transactional(readOnly = true)
    public List<ReadHashtagWithUsageCountResponse> getTopHashtagsByUsageCount(final int limit) {
        validateLimit(limit);
        final List<HashtagPopularityProjection> projections =
                bottariTemplateHashtagRepository.findTopNByUsageCount(limit);

        return projections.stream()
                .map(ReadHashtagWithUsageCountResponse::of)
                .toList();
    }

    private void validateLimit(final int limit) {
        if (limit <= 0) {
            throw new BusinessException(ErrorCode.HASHTAG_LIMIT_TOO_LOW, "조회는 1개 이상 가능합니다.");
        }
        if (limit > 100) {
            throw new BusinessException(ErrorCode.HASHTAG_LIMIT_TOO_HIGH, "조회는 100개 이하 가능합니다.");
        }
    }
}
