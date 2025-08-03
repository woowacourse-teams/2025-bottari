package com.bottari.service.fixture;

import com.bottari.domain.Bottari;
import com.bottari.domain.Member;

public enum BottariFixture {

    BOTTARI("title"),
    BOTTARI_2("title2"),
    ANOTHER_BOTTARI("another title");
    ;

    private final String title;

    BottariFixture(final String title) {
        this.title = title;
    }

    public Bottari get(final Member member) {
        return new Bottari(title, member);
    }
}
