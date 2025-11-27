package com.bottari.sse.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSubscribeManager {

    private final RedisMessageListenerContainer container;
    private final SseMessageListener sseMessageListener;

    public void subscribe(final MemberChannelTopic topic) {
        container.removeMessageListener(sseMessageListener, topic);
        container.addMessageListener(sseMessageListener, topic);
    }

    public void unsubscribe(final MemberChannelTopic topic) {
        container.removeMessageListener(sseMessageListener, topic);
    }
}
