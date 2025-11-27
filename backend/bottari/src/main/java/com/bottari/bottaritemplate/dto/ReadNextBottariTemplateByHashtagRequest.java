package com.bottari.bottaritemplate.dto;

import com.bottari.bottaritemplate.domain.BottariTemplateHashtagCursor;

public record ReadNextBottariTemplateByHashtagRequest(
        Long hashtagId,
        Long lastId,
        String lastInfo,
        int page,
        int size,
        String property
) {

    public BottariTemplateHashtagCursor toCursor() {
        return new BottariTemplateHashtagCursor(
                hashtagId,
                lastId,
                lastInfo,
                page,
                size,
                property
        );
    }
}
