package com.bottari.bottaritemplate.dto;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.bottaritemplate.domain.BottariTemplateItem;
import com.bottari.bottaritemplate.domain.Hashtag;
import com.bottari.bottaritemplate.repository.dto.BottariTemplateProjection;
import java.time.LocalDateTime;
import java.util.List;

public record ReadBottariTemplateResponse(
        Long id,
        String title,
        String description,
        List<BottariTemplateItemResponse> items,
        String author,
        LocalDateTime createdAt,
        int takenCount,
        List<HashtagResponse> hashtags
) {

    public static ReadBottariTemplateResponse of(
            final BottariTemplate bottariTemplate,
            final List<BottariTemplateItem> bottariTemplateItems,
            final List<Hashtag> hashtags
    ) {
        final List<BottariTemplateItemResponse> items = bottariTemplateItems.stream()
                .map(BottariTemplateItemResponse::from)
                .toList();

        return new ReadBottariTemplateResponse(
                bottariTemplate.getId(),
                bottariTemplate.getTitle(),
                bottariTemplate.getDescription(),
                items,
                bottariTemplate.getMember().getName(),
                bottariTemplate.getCreatedAt(),
                bottariTemplate.getTakenCount(),
                hashtags.stream().map(HashtagResponse::from).toList()
        );
    }

    public static ReadBottariTemplateResponse of(
            final BottariTemplateProjection projection,
            final List<BottariTemplateItem> bottariTemplateItems,
            final List<Hashtag> hashtags
    ) {
        final List<BottariTemplateItemResponse> items = bottariTemplateItems.stream()
                .map(BottariTemplateItemResponse::from)
                .toList();

        return new ReadBottariTemplateResponse(
                projection.getBottariTemplateId(),
                projection.getTitle(),
                projection.getDescription(),
                items,
                projection.getMemberName(),
                projection.getBottariTemplateCreatedAt(),
                projection.getTakenCount(),
                hashtags.stream().map(HashtagResponse::from).toList()
        );
    }

    public record BottariTemplateItemResponse(
            Long id,
            String name
    ) {

        public static BottariTemplateItemResponse from(final BottariTemplateItem bottariTemplateItem) {
            return new BottariTemplateItemResponse(
                    bottariTemplateItem.getId(),
                    bottariTemplateItem.getName()
            );
        }
    }

    public record HashtagResponse(
            Long id,
            String name
    ) {
        public static HashtagResponse from(final Hashtag hashtag) {
            return new HashtagResponse(
                    hashtag.getId(),
                    hashtag.getName()
            );
        }
    }
}
