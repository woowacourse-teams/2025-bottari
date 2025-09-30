package com.bottari.push.message;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PushMessage {

    private final String resource;
    private final String event;
    private final Object data;
    private final LocalDateTime publishedAt;

    public PushMessage(
            final MessageResourceType resource,
            final MessageEventType event,
            final Object data
    ) {
        this.resource = resource.name();
        this.event = event.name();
        this.data = data;
        this.publishedAt = LocalDateTime.now();
    }
}
