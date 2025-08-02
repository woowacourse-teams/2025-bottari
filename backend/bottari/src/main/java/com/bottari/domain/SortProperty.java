package com.bottari.domain;

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

    public boolean equalsProperty(final String property) {
        return this.property.equals(property);
    }

    public static SortProperty fromProperty(final String property) {
        for (final SortProperty value : SortProperty.values()) {
            if (value.equalsProperty(property)) {
                return value;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 정렬 기준입니다.");
    }
}
