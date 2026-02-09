package com.bottari.sse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bottari.openapi.server")
public record OpenApiServerProperties(
    String url,
    String description
) {
}