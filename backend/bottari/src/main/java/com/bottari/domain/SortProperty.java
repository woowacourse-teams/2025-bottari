package com.bottari.domain;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 정렬 기준입니다."));
    }

    public boolean equalsProperty(final String property) {
        return this.property.equals(property);
    }
}
