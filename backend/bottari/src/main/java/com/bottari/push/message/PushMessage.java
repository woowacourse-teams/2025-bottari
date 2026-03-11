package com.bottari.push.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public PushMessage(
            final String resource,
            final String event,
            final Object data
    ) {
        this.resource = resource;
        this.event = event;
        this.data = data;
        this.publishedAt = LocalDateTime.now();
    }

    @JsonCreator
    public PushMessage(
            @JsonProperty("resource") final String resource,
            @JsonProperty("event") final String event,
            @JsonProperty("data") final Object data,
            @JsonProperty("publishedAt") final LocalDateTime publishedAt
    ) {
        this.resource = resource;
        this.event = event;
        this.data = data;
        this.publishedAt = publishedAt;
    }
}
