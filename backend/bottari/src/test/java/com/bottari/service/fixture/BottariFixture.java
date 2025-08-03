package com.bottari.service.fixture;

import com.bottari.domain.Bottari;
import com.bottari.domain.Member;

public enum BottariFixture {

    BOTTARI("title"),
    ANOTHER_BOTTARI("another title");
    ;

    private final String title;

    BottariFixture(final String title) {
        this.title = title;
    }

    public Bottari getBottari(final Member member) {
        return new Bottari(title, member);
    }
}
