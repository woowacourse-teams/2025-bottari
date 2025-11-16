package com.bottari.sse.sse;

import com.bottari.sse.error.BusinessException;
import com.bottari.sse.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AsyncSseProcessor asyncSseProcessor;

    @Override
    public void onMessage(
            final Message message,
            final byte[] pattern
    ) {
        final MemberChannelTopic topic =
                new MemberChannelTopic(new String(message.getChannel(), StandardCharsets.UTF_8));
        try {
            final PubSubEnvelope pubSubEnvelope = objectMapper.readValue(message.getBody(), PubSubEnvelope.class);
            final Long memberId = topic.extractMemberId();
            asyncSseProcessor.processAsync(pubSubEnvelope, memberId);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INVALID_MESSAGE_FORMAT);
        }
    }
}
