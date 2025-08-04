package com.bottari.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record BottariTemplateCursor(
        String query,
        Long lastId,
        String lastInfo,
        int page,
        int size,
        String property
) {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final int DEFAULT_SIZE = 10;

    public BottariTemplateCursor {
        query = normalizeQuery(query);
        size = normalizeSize(size);
        page = normalizePage(page);
        lastId = normalizeLastId(lastId);
        property = normalizeProperty(property);
        lastInfo = normalizeLastInfo(property, lastInfo);
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }

    public LocalDateTime getCreatedAt() {
        try {
            return LocalDateTime.parse(lastInfo, DATE_TIME_FORMATTER);
        } catch (final DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형태가 올바르지 않습니다.");
        }
    }

    public Long getTakenCount() {
        try {
            return Long.parseLong(lastInfo);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("숫자 형태가 올바르지 않습니다.");
        }
    }

    private static String normalizeQuery(final String query) {
        if (query == null || query.isBlank()) {
            return "";
        }

        return query;
    }

    private static int normalizeSize(final int size) {
        if (size <= 0) {
            return DEFAULT_SIZE;
        }

        return size;
    }

    private static int normalizePage(final int page) {
        if (page < 0) {
            return 0;
        }

        return page;
    }

    private static Long normalizeLastId(final Long lastId) {
        if (lastId == null) {
            return Long.MAX_VALUE;
        }

        return lastId;
    }

    private static String normalizeProperty(final String property) {
        if (property == null || property.isBlank()) {
            return SortProperty.CREATED_AT.getProperty();
        }

        return property;
    }

    private static String normalizeLastInfo(
            final String property,
            final String lastInfo
    ) {
        if (SortProperty.CREATED_AT.equalsProperty(property) && (lastInfo == null || lastInfo.isBlank())) {
            return LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER);
        }
        if (SortProperty.TAKEN_COUNT.equalsProperty(property) && (lastInfo == null || lastInfo.isBlank())) {
            return String.valueOf(Long.MAX_VALUE);
        }

        return lastInfo;
    }
}
