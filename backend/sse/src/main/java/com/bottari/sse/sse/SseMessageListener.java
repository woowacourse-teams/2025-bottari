package com.bottari.sse.sse;

import com.bottari.sse.error.BusinessException;
import com.bottari.sse.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseMessageListener implements MessageListener {

    private final SseChannel sseChannel;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(
            final Message message,
            final byte[] pattern
    ) {
        final RedisTopic topic = new RedisTopic(new String(message.getChannel()));
        try {
            final PushMessage pushMessage = objectMapper.readValue(message.getBody(), PushMessage.class);
            sseChannel.unicast(pushMessage, topic.extractMemberId());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INVALID_MESSAGE_FORMAT);
        }
    }
}
