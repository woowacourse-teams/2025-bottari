package com.bottari.bottaritemplate.dto;

import java.util.List;

public record CreateBottariTemplateRequest(
        String title,
        String description,
        List<String> bottariTemplateItems
) {
}
