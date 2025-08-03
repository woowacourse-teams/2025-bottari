package com.bottari.service.fixture;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.Member;

public enum BottariTemplateFixture {

    BOTTARI_TEMPLATE("title");

    private final String title;

    BottariTemplateFixture(final String title) {
        this.title = title;
    }

    public BottariTemplate getBottariTemplate(final Member member) {
        return new BottariTemplate(title, member);
    }
}
