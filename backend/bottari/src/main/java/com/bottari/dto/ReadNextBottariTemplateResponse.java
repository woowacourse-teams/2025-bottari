package com.bottari.dto;

import com.bottari.domain.SortProperty;
import java.util.List;
import org.springframework.data.domain.Slice;

public record ReadNextBottariTemplateResponse(
        List<ReadBottariTemplateResponse> contents,
        int currentPage,
        int size,
        boolean hasNext,
        boolean first,
        boolean last,
        String sortProperty,
        Long lastId,
        String lastInfo
) {

    public static ReadNextBottariTemplateResponse of(
            final Slice<ReadBottariTemplateResponse> slice,
            final String property
    ) {
        final List<ReadBottariTemplateResponse> content = slice.getContent();
        final SortProperty sortProperty = SortProperty.fromProperty(property);
        if (content.isEmpty()) {
            return emptyContentResponse(slice, sortProperty);
        }
        final Long lastId = content.getLast().id();
        final String lastInfo = switch (sortProperty) {
            case CREATED_AT -> content.getLast().createAt().toString();
            case TAKEN_COUNT -> String.valueOf(content.getLast().takenCount());
        };

        return new ReadNextBottariTemplateResponse(
                content,
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                slice.isFirst(),
                slice.isLast(),
                sortProperty.getProperty(),
                lastId,
                lastInfo
        );
    }

    private static ReadNextBottariTemplateResponse emptyContentResponse(
            final Slice<ReadBottariTemplateResponse> slice,
            final SortProperty sortProperty
    ) {
        return new ReadNextBottariTemplateResponse(
                List.of(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                slice.isFirst(),
                slice.isLast(),
                sortProperty.getProperty(),
                null,
                null
        );
    }
}
