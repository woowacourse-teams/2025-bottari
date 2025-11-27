package com.bottari.fixture;

import com.bottari.bottaritemplate.domain.Hashtag;

public enum HashtagFixture {

    HASHTAG_1("hashtag1"),
    HASHTAG_2("hashtag2"),
    HASHTAG_3("hashtag3"),
    ;

    private final String name;

    HashtagFixture(final String name) {
        this.name = name;
    }

    public Hashtag get() {
        return new Hashtag(name);
    }
}
