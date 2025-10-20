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
        sseEmitter.onCompletion(() -> cleanup(topic, memberId));
        sseEmitter.onTimeout(() -> cleanup(topic, memberId));
        sseEmitter.onError(t -> cleanup(topic, memberId));
        sseSessions.save(memberId, sseEmitter);
        subscribeManager.subscribe(topic);
    }

    private void cleanup(
            final MemberChannelTopic topic,
            final Long memberId
    ) {
        try {
            subscribeManager.unsubscribe(topic);
        } finally {
            sseSessions.remove(memberId);
        }
    }
}
