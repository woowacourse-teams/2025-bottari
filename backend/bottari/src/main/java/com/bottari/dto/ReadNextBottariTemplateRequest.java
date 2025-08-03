package com.bottari.dto;

public record ReadNextBottariTemplateRequest(
        String query,
        Long lastId,
        String lastInfo,
        int page,
        int size,
        String property
) {
}
