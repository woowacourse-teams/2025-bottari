package com.bottari.push.connection.sse.external;

import com.bottari.push.ChannelType;
import com.bottari.push.connection.sse.SseChannel;
import com.bottari.push.message.PushMessage;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RedisSseChannel implements SseChannel {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void unicast(
            final PushMessage message,
            final Long memberId
    ) {
        final MemberChannelTopic topic = MemberChannelTopic.from(memberId);
        publish(message, topic);
    }

    @Override
    public void multicast(
            final PushMessage message,
            final List<Long> memberIds
    ) {
        memberIds.forEach(memberId -> unicast(message, memberId));
    }

    @Override
    public void broadcast(final PushMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.SSE;
    }

    @WithSpan(value = "redis publish", kind = SpanKind.PRODUCER)
    private void publish(
            final PushMessage message,
            final MemberChannelTopic topic
    ) {
        final Map<String, String> headers = new HashMap<>();
        GlobalOpenTelemetry.getPropagators().getTextMapPropagator()
                .inject(Context.current(), headers, Map::put);
        final PubSubEnvelope envelope = new PubSubEnvelope(headers, message);
        redisTemplate.convertAndSend(topic.getTopic(), envelope);
    }
}
