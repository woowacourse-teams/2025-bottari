package com.bottari.bottaritemplate.dto;

import java.util.List;

public record CreateBottariTemplateRequest(
        String title,
        List<String> bottariTemplateItems
) {
}
