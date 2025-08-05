package com.bottari.service.fixture;

import com.bottari.domain.BottariTemplate;
import com.bottari.domain.BottariTemplateItem;

public enum BottariTemplateItemFixture {

    BOTTARI_TEMPLATE_ITEM_1("name1"),
    BOTTARI_TEMPLATE_ITEM_2("name2"),
    BOTTARI_TEMPLATE_ITEM_3("name3"),
    BOTTARI_TEMPLATE_ITEM_4("name4")
    ;

    private final String name;

    BottariTemplateItemFixture(final String name) {
        this.name = name;
    }

    public BottariTemplateItem get(final BottariTemplate bottariTemplate) {
        return new BottariTemplateItem(name, bottariTemplate);
    }
}
