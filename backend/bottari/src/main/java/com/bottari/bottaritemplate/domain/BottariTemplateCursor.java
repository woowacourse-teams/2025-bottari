package com.bottari.bottaritemplate.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
public abstract class BottariTemplateCursor {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private static final int DEFAULT_SIZE = 10;

    private final Long lastId;
    private final String lastInfo;
    private final int page;
    private final int size;
    private final String property;

    public BottariTemplateCursor(
            final Long lastId,
            final String lastInfo,
            final int page,
            final int size,
            final String property
    ) {
        this.size = normalizeSize(size);
        this.page = normalizePage(page);
        this.lastId = normalizeLastId(lastId);
        this.property = normalizeProperty(property);
        this.lastInfo = normalizeLastInfo(property, lastInfo);
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
            return String.valueOf(Integer.MAX_VALUE);
        }

        return lastInfo;
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }

    public LocalDateTime getCreatedAt() {
        try {
            return LocalDateTime.parse(lastInfo, DATE_TIME_FORMATTER);
        } catch (final DateTimeParseException e) {
            throw new BusinessException(
                    ErrorCode.DATE_FORMAT_INVALID,
                    "보따리 템플릿의 생성일자는 (%s) 형식이어야 합니다.".formatted(DATE_TIME_PATTERN)
            );
        }
    }

    public Integer getTakenCount() {
        try {
            return Integer.parseInt(lastInfo);
        } catch (final NumberFormatException e) {
            throw new BusinessException(ErrorCode.NUMBER_FORMAT_INVALID, "보따리 템플릿의 가져간 횟수는 숫자여야 합니다.");
        }
    }
}
