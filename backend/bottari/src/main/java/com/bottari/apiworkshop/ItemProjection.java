package com.bottari.apiworkshop;

import com.bottari.vo.ItemName;

public interface ItemProjection {

    ItemName getName();

    Long getIncludedCount();
}
