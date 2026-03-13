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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SseMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SseSendExecutor sseSendExecutor;
    private final SseChannel sseChannel;

    @Override
    public void onMessage(
            final Message message,
            final byte[] pattern
    ) {
        final MemberChannelTopic topic =
                new MemberChannelTopic(new String(message.getChannel(), StandardCharsets.UTF_8));
        try {
            final PubSubEnvelope pubSubEnvelope = objectMapper.readValue(message.getBody(), PubSubEnvelope.class);
            final Context parent = GlobalOpenTelemetry.getPropagators()
                    .getTextMapPropagator()
                    .extract(Context.current(), pubSubEnvelope.headers(), MapGetter.INSTANCE);
            try (final Scope ignored = parent.makeCurrent()) {
                final SseSendTask task = new SseSendTask(sseChannel, pubSubEnvelope.payload(), topic);
                sseSendExecutor.submit(task);
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INVALID_MESSAGE_FORMAT);
        }
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
