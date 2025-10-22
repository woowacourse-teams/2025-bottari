package com.bottari.push.connection.sse.inmemory;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Profile("(test1 | test3)")
@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

    private final SseSessions sseSessions;

    @Scheduled(fixedRate = 15_000) // 15초
    public void sendHeartbeats() {
        final List<SseEmitter> emitters = sseSessions.findAll();
        if (emitters.isEmpty()) {
            return;
        }

        for (final SseEmitter emitter : emitters) {
            try {
                emitter.send(
                        SseEmitter.event().comment(":hb")
                );
            } catch (IOException | IllegalStateException e) {
                emitter.completeWithError(e);
            }
        }
    }
}
