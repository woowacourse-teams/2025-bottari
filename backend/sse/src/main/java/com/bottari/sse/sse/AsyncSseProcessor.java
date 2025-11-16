package com.bottari.sse.sse;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.annotation.Nullable;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncSseProcessor {

    private final SseChannel sseChannel;

    @Async("sseTaskExecutor")
    public void processAsync(
            final PubSubEnvelope envelope,
            final Long memberId
    ) {
        final Context parent = GlobalOpenTelemetry.getPropagators()
                .getTextMapPropagator()
                .extract(Context.current(), envelope.headers(), MapGetter.INSTANCE);

        try (final Scope ignored = parent.makeCurrent()) {
            sendMessage(envelope.payload(), memberId);
        }
    }

    @WithSpan(value = "redis subscriber", kind = SpanKind.CONSUMER)
    private void sendMessage(
            final PushMessage pushMessage,
            final Long memberId
    ) {
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
