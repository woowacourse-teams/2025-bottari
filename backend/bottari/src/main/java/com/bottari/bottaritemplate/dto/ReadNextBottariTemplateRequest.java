package com.bottari.bottaritemplate.dto;

import com.bottari.bottaritemplate.domain.BottariTemplateCursor;

public record ReadNextBottariTemplateRequest(
        String query,
        Long lastId,
        String lastInfo,
        int page,
        int size,
        String property
) {

    public BottariTemplateCursor toCursor() {
        return new BottariTemplateCursor(
                query,
                lastId,
                lastInfo,
                page,
                size,
                property
        );
    }
}
