package com.bottari.sse.sse;

import com.bottari.sse.error.BusinessException;
import com.bottari.sse.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncSseProcessor {

    private final ObjectMapper objectMapper;
    private final SseChannel sseChannel;

    @Async("sseTaskExecutor")
    public void processAsync(
            final Message message
    ) {
        final MemberChannelTopic topic =
                new MemberChannelTopic(new String(message.getChannel(), StandardCharsets.UTF_8));
        try {
            final PubSubEnvelope pubSubEnvelope = objectMapper.readValue(message.getBody(), PubSubEnvelope.class);
            final Context parent = GlobalOpenTelemetry.getPropagators()
                    .getTextMapPropagator()
                    .extract(Context.current(), pubSubEnvelope.headers(), MapGetter.INSTANCE);
            try (final Scope ignored = parent.makeCurrent()) {
                consume(pubSubEnvelope.payload(), topic);
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INVALID_MESSAGE_FORMAT);
        }
    }

    @WithSpan(value = "redis subscriber", kind = SpanKind.CONSUMER)
    private void consume(
            final PushMessage pushMessage,
            final MemberChannelTopic topic
    ) {
        final Long memberId = topic.extractMemberId();
        sseChannel.unicast(pushMessage, memberId);
    }

    private enum MapGetter implements TextMapGetter<Map<String, String>> {
        INSTANCE;

        @Override
        public Iterable<String> keys(final Map<String, String> map) {
            return map.keySet();
        }

        @Override
        public String get(
                final Map<String, String> map,
                @Nullable final String key
        ) {
            if (map == null) {
                return null;
            }

            return map.get(key);
        }
    }
}
