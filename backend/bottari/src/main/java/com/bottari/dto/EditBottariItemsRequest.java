package com.bottari.dto;

import java.util.List;

public record EditBottariItemsRequest(
        List<Long> deleteItemIds,
        List<String> createItemNames
) {
}
