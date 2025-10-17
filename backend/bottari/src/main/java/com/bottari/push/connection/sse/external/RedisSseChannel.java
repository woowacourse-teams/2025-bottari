package com.bottari.push.connection.sse.external;

import com.bottari.push.ChannelType;
import com.bottari.push.connection.sse.SseChannel;
import com.bottari.push.message.PushMessage;
import java.util.List;
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
        redisTemplate.convertAndSend("SSE", message);
    }

    @Override
    public void multicast(
            final PushMessage message,
            final List<Long> memberIds
    ) {
        redisTemplate.convertAndSend("SSE", message);
    }

    @Override
    public void broadcast(final PushMessage message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChannelType channelType() {
        return ChannelType.SSE;
    }
}
