package com.bottari.bottaritemplate.domain;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class BottariTemplateTitleCursor extends BottariTemplateCursor {

    private final String title;

    public BottariTemplateTitleCursor(
            final String title,
            final Long lastId,
            final String lastInfo,
            final int page,
            final int size,
            final String property
    ) {
        super(lastId, lastInfo, page, size, property);
        this.title = normalizeTile(title);
    }

    private String normalizeTile(final String title) {
        if (title == null || title.isBlank()) {
            return "";
        }

        return Arrays.stream(title.trim().split("\\s+"))
                .filter(word -> !word.isBlank())
                .map(word -> "+" + word)
                .collect(Collectors.joining(" "));
    }
}
