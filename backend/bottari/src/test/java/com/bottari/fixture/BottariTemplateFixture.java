package com.bottari.fixture;

import com.bottari.bottaritemplate.domain.BottariTemplate;
import com.bottari.member.domain.Member;

public enum BottariTemplateFixture {

    BOTTARI_TEMPLATE("title"),
    BOTTARI_TEMPLATE_2("title2"),
    ANOTHER_BOTTARI_TEMPLATE("another title"),
    ;

    private final String title;

    BottariTemplateFixture(final String title) {
        this.title = title;
    }

    public BottariTemplate get(final Member member) {
        return new BottariTemplate(title, member);
    }
}
