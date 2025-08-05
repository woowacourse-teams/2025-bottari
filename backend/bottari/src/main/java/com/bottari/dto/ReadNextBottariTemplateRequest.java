package com.bottari.dto;

import com.bottari.domain.BottariTemplateCursor;

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
