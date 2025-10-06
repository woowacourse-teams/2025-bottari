package com.bottari.bottaritemplate.dto;

import com.bottari.bottaritemplate.domain.BottariTemplateTitleCursor;

public record ReadNextBottariTemplateByTitleRequest(
        String query,
        Long lastId,
        String lastInfo,
        int page,
        int size,
        String property
) {

    public BottariTemplateTitleCursor toCursor() {
        return new BottariTemplateTitleCursor(
                query,
                lastId,
                lastInfo,
                page,
                size,
                property
        );
    }
}
