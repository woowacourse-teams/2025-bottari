package com.bottari.sse.sse;

import java.util.Collections;
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

    public List<SseEmitter> findAll() {
        return sseEmittersByMemberId.values().stream().toList();
    }

    public Map<Long, SseEmitter> findAllWithMemberId() {
        return Collections.unmodifiableMap(sseEmittersByMemberId);
    }

    public void save(
            final Long memberId,
            final SseEmitter sseEmitter
    ) {
        final SseEmitter prevSseEmitter = sseEmittersByMemberId.put(memberId, sseEmitter);
        if (prevSseEmitter != null) {
            prevSseEmitter.complete();
        }
    }

    public boolean removeIfSame(
            final Long memberId,
            final SseEmitter sseEmitter
    ) {
        return sseEmittersByMemberId.remove(memberId, sseEmitter);
    }
}
