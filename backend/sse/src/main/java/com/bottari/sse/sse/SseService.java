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

        sseEmitter.onCompletion(() -> cleanUpIfSame(topic, memberId, sseEmitter));
        sseEmitter.onTimeout(() -> cleanUpIfSame(topic, memberId, sseEmitter));
        sseEmitter.onError(t -> cleanUpIfSame(topic, memberId, sseEmitter));

        sseSessions.save(memberId, sseEmitter);
        subscribeManager.subscribe(topic);
    }

    private void cleanUpIfSame(
            final MemberChannelTopic topic,
            final Long memberId,
            final SseEmitter targetEmitter
    ) {
        final boolean removed = sseSessions.removeIfSame(memberId, targetEmitter);
        if (removed && sseSessions.findByMemberId(memberId).isEmpty()) {
            subscribeManager.unsubscribe(topic);
        }
    }
}
