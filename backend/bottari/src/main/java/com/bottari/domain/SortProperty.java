package com.bottari.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum SortProperty {

    CREATED_AT("createdAt"),
    TAKEN_COUNT("takenCount"),
    ;

    private final String property;

    SortProperty(final String property) {
        this.property = property;
    }

    public static SortProperty fromProperty(final String property) {
        return Arrays.stream(SortProperty.values())
                .filter(value -> value.equalsProperty(property))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.BOTTARI_TEMPLATE_INVALID_SORT_TYPE));
    }

    public boolean equalsProperty(final String property) {
        return this.property.equals(property);
    }
}
