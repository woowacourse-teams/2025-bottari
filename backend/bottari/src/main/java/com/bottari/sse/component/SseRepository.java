package com.bottari.sse.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
                key -> new ArrayList<>()
        ).add(sseEmitter);
    }
}
