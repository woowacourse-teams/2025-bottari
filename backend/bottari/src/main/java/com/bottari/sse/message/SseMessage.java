package com.bottari.sse.message;

import lombok.Getter;

@Getter
public class SseMessage {

    private final String resource;
    private final String event;
    private final Object data;

    public SseMessage(
            final SseResourceType resource,
            final SseEventType event,
            final Object data
    ) {
        this.resource = resource.name();
        this.event = event.name();
        this.data = data;
    }
}
