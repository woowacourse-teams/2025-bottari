package com.bottari.sse.message;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SseMessage {

    private final LocalDateTime publishedAt;
    private final String resource;
    private final String event;
    private final Object data;

    public SseMessage(
            final SseResourceType resource,
            final SseEventType event,
            final Object data
    ) {
        this.publishedAt = LocalDateTime.now();
        this.resource = resource.name();
        this.event = event.name();
        this.data = data;
    }
}
