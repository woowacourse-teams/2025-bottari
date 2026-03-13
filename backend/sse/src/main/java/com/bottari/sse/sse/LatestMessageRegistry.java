package com.bottari.sse.sse;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class LatestMessageRegistry {

    private final Map<DeliveryKey, LocalDateTime> latestMessages = new ConcurrentHashMap<>();

    public void register(
            final DeliveryKey key,
            final LocalDateTime messageId
    ) {
        latestMessages.put(key, messageId);
    }

    public boolean removeIfSame(
            final DeliveryKey key,
            final LocalDateTime messageId
    ) {
        return latestMessages.remove(key, messageId);
    }
}
