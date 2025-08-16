package com.bottari.sse.component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseRepository {

    private final Map<Long, List<SseEmitter>> teamBottariEmitters = new ConcurrentHashMap<>();

    public List<SseEmitter> findByTeamBottariId(final Long teamBottariId) {
        return teamBottariEmitters.getOrDefault(teamBottariId, List.of());
    }

    public void save(
            final Long teamBottariId,
            final SseEmitter sseEmitter
    ) {
        teamBottariEmitters.computeIfAbsent(
                teamBottariId,
                key -> new CopyOnWriteArrayList<>()
        ).add(sseEmitter);
    }

    public void remove(
            final Long teamBottariId,
            final SseEmitter sseEmitter
    ) {
        final List<SseEmitter> emitters = teamBottariEmitters.get(teamBottariId);
        if (emitters != null) {
            emitters.remove(sseEmitter);
            if (emitters.isEmpty()) {
                teamBottariEmitters.remove(teamBottariId);
            }
        }
    }
}
