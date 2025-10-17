package com.bottari.sse.sse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public final class SseChannel {

    private final SseSessions sseSessions;

    public void unicast(
            final PushMessage message,
            final Long memberId
    ) {
        final Optional<SseEmitter> sseEmitterOptional = sseSessions.findByMemberId(memberId);
        if (sseEmitterOptional.isEmpty()) {
            return;
        }
        final SseEmitter sseEmitter = sseEmitterOptional.get();
        try {
            sseEmitter.send(message, MediaType.APPLICATION_JSON);
        } catch (final IOException | IllegalStateException e) {
            sseEmitter.completeWithError(e);
        }
    }

    public void multicast(
            final PushMessage message,
            final List<Long> memberIds
    ) {
        final List<SseEmitter> sseEmitters = sseSessions.findAllByMemberIds(memberIds);
        for (final SseEmitter sseEmitter : sseEmitters) {
            try {
                sseEmitter.send(message, MediaType.APPLICATION_JSON);
            } catch (final IOException | IllegalStateException e) {
                sseEmitter.completeWithError(e);
            }
        }
    }

    public void broadcast(final PushMessage message) {
        throw new UnsupportedOperationException();
    }
}
