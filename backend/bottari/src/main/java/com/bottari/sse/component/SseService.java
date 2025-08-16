package com.bottari.sse.component;

import com.bottari.sse.message.SseMessage;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;

    public void sendByTeamBottariId(
            final Long teamBottariId,
            final SseMessage message
    ) {
        final List<SseEmitter> sseEmitters = sseRepository.findByTeamBottariId(teamBottariId);
        for (final SseEmitter sseEmitter : sseEmitters) {
            try {
                sseEmitter.send(message, MediaType.APPLICATION_JSON);
            } catch (final IOException e) {
                sseEmitter.complete();
                sseRepository.remove(teamBottariId, sseEmitter);
            }
        }
    }

    public void register(
            final Long teamBottariId,
            final SseEmitter sseEmitter
    ) {
        sseRepository.save(teamBottariId, sseEmitter);
    }
}
