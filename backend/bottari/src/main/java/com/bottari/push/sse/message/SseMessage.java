package com.bottari.push.sse.message;

import com.bottari.push.ChannelType;
import com.bottari.push.PushMessage;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SseMessage implements PushMessage {

    private final String resource;
    private final String event;
    private final Object data;
    private final LocalDateTime publishedAt;

    public SseMessage(
            final SseResourceType resource,
            final SseEventType event,
            final Object data
    ) {
        this.resource = resource.name();
        this.event = event.name();
        this.data = data;
        this.publishedAt = LocalDateTime.now();
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.SSE;
    }
}
