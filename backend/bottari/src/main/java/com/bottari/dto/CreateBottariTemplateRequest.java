package com.bottari.dto;

import java.util.List;

public record CreateBottariTemplateRequest(
        String title,
        List<String> bottariTemplateItems
) {
}
