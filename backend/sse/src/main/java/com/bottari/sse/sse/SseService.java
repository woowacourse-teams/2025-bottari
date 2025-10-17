package com.bottari.sse.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseSessions sseSessions;
    private final RedisSubscribeManager subscribeManager;

    public void register(
            final Long memberId,
            final SseEmitter sseEmitter
    ) {
        final MemberChannelTopic topic = MemberChannelTopic.from(memberId);
        sseEmitter.onCompletion(() -> {
            subscribeManager.unsubscribe(topic);
            sseSessions.remove(memberId);
        });
        sseEmitter.onTimeout(() -> {
            subscribeManager.unsubscribe(topic);
            sseSessions.remove(memberId);
        });
        sseEmitter.onError(throwable -> {
            subscribeManager.unsubscribe(topic);
            sseSessions.remove(memberId);
        });
        subscribeManager.subscribe(topic);
        sseSessions.save(memberId, sseEmitter);
    }
}
