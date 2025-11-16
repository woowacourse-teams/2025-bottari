package com.bottari.sse.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseMessageListener implements MessageListener {

    private final AsyncSseProcessor asyncSseProcessor;

    @Override
    public void onMessage(
            final Message message,
            final byte[] pattern
    ) {
        asyncSseProcessor.processAsync(message);
    }
}
