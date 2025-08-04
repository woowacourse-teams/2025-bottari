package com.bottari.dto;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;
import java.util.List;

public record ReadBottariTemplateResponse(
        Long id,
        String title,
        List<BottariTemplateItemResponse> items,
        String author,
        int takenCount
) {

    public static ReadBottariTemplateResponse of(
            final BottariTemplate bottariTemplate,
            final List<BottariTemplateItem> bottariTemplateItems
    ) {
        final List<BottariTemplateItemResponse> items = bottariTemplateItems.stream()
                .map(BottariTemplateItemResponse::from)
                .toList();

        return new ReadBottariTemplateResponse(
                bottariTemplate.getId(),
                bottariTemplate.getTitle(),
                items,
                bottariTemplate.getMember().getName(),
                bottariTemplate.getTakenCount()
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
}
