package com.bottari.sse.sse;

import java.time.LocalDateTime;

public record PushMessage(
        String resource,
        String event,
        Object data,
        LocalDateTime publishedAt
) {
}
