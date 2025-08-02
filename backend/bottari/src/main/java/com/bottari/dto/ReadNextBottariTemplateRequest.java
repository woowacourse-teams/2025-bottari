package com.bottari.dto;

import com.bottari.domain.SortProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record ReadNextBottariTemplateRequest(
        String query,
        Long lastId,
        String lastInfo,
        int page,
        int size,
        String property
) {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public ReadNextBottariTemplateRequest {
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
        return LocalDateTime.parse(lastInfo, DATE_TIME_FORMATTER);
    }

    public Long getTakenCount() {
        return Long.parseLong(lastInfo);
    }

    private String normalizeQuery(final String query) {
        if (query == null || query.isBlank()) {
            return "";
        }
        return query;
    }

    private int normalizeSize(final int size) {
        if (size <= 0) {
            return 10;
        }
        return size;
    }

    private int normalizePage(final int page) {
        if (page < 0) {
            return 0;
        }
        return page;
    }

    private Long normalizeLastId(final Long lastId) {
        if (lastId == null) {
            return Long.MAX_VALUE;
        }
        return lastId;
    }

    private String normalizeProperty(final String property) {
        if (property == null || property.isBlank()) {
            return SortProperty.CREATED_AT.getProperty();
        }
        return property;
    }

    private String normalizeLastInfo(
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
