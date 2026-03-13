package com.bottari.sse.sse;

import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class SseHeartbeatScheduler {

    private final SseSessions sseSessions;
    private final SseRefreshRegistry sseRefreshRegistry;

    @Scheduled(fixedRate = 15_000) // 15초
    public void sendHeartbeats() {
        final Map<Long, SseEmitter> emitters = sseSessions.findAllWithMemberId();
        if (emitters.isEmpty()) {
            return;
        }

        for (final Long memberId : emitters.keySet()) {
            final SseEmitter emitter = emitters.get(memberId);
            try {
                if (sseRefreshRegistry.needRefresh(memberId)) {
                    emitter.send(
                            SseEmitter.event().comment(":hb").data(Map.of("refresh", true))
                    );
                    continue;
                }
                emitter.send(
                        SseEmitter.event().comment(":hb")
                );
            } catch (IOException | IllegalStateException e) {
                emitter.completeWithError(e);
            }
        }
    }
}
