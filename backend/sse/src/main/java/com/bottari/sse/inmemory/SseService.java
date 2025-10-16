package com.bottari.sse.inmemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseSessions sseSessions;

    public void register(
            final Long memberId,
            final SseEmitter sseEmitter
    ) {
        sseEmitter.onCompletion(() -> sseSessions.remove(memberId));
        sseEmitter.onTimeout(() -> sseSessions.remove(memberId));
        sseEmitter.onError(throwable -> sseSessions.remove(memberId));
        sseSessions.save(memberId, sseEmitter);
    }
}
