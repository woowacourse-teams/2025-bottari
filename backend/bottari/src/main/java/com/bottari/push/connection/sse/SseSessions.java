package com.bottari.push.connection.sse;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseSessions {

    private final Map<Long, SseEmitter> sseEmittersByMemberId = new ConcurrentHashMap<>();

    public Optional<SseEmitter> findByMemberId(final Long memberId) {
        final SseEmitter emitter = sseEmittersByMemberId.get(memberId);

        return Optional.ofNullable(emitter);
    }

    public List<SseEmitter> findAllByMemberIds(final List<Long> memberIds) {
        return memberIds.stream()
                .map(sseEmittersByMemberId::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void save(
            final Long memberId,
            final SseEmitter sseEmitter
    ) {
        sseEmittersByMemberId.put(memberId, sseEmitter);
    }

    public void remove(final Long memberId) {
        sseEmittersByMemberId.remove(memberId);
    }
}
