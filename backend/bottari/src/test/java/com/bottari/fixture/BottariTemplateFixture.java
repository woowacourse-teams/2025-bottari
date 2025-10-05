package com.bottari.fixture;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.member.domain.Member;

public enum BottariTemplateFixture {

    BOTTARI_TEMPLATE("title", "description"),
    BOTTARI_TEMPLATE_2("title2", "description2"),
    ANOTHER_BOTTARI_TEMPLATE("another title", "another description"),
    ;

    private final String title;
    private final String description;

    BottariTemplateFixture(
            final String title,
            final String description
    ) {
        this.title = title;
        this.description = description;
    }

    public BottariTemplate get(final Member member) {
        return new BottariTemplate(title, description, member);
    }
}
