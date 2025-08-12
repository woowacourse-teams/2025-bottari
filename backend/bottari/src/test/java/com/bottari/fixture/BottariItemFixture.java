package com.bottari.fixture;

import com.bottari.bottari.domain.Bottari;
import com.bottari.bottari.domain.BottariItem;

public enum BottariItemFixture {

    BOTTARI_ITEM_1("name1"),
    BOTTARI_ITEM_2("name2"),
    BOTTARI_ITEM_3("name3"),
    ;

    private final String name;

    BottariItemFixture(final String name) {
        this.name = name;
    }

    public BottariItem get(final Bottari bottari) {
        return new BottariItem(name, bottari);
    }
}
